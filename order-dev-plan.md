# Order 域开发计划

## 一、现状分析

### 1.1 后端接口状态

| # | 方法 | 路径 | 状态 | 问题 |
|---|------|------|------|------|
| 1 | POST | `/api/order/create` | ⚠️ 半成品 | 订单号用 `goods.getId()` 不唯一；未锁定商品状态 |
| 2 | POST | `/api/order/buy` | ⚠️ 半成品 | 未创建支付记录；未写钱包流水；未锁定商品 |
| 3 | GET | `/api/order/{id}` | ❌ 桩代码 | 返回空，无数据 |
| 4 | POST | `/api/order/{id}/send` | ❌ 桩代码 | 仅打日志，未更新状态/时间 |
| 5 | POST | `/api/order/{id}/confirm` | ❌ 桩代码 | 仅打日志，未转账/改状态/改商品 |
| 6 | POST | `/api/order/{id}/cancel` | ❌ 桩代码 | 仅打日志，未恢复状态/解冻余额 |
| 7 | POST | `/api/order/refund/apply` | ❌ 桩代码 | 参数是 `Object`，未创建退款记录 |
| 8 | GET | `/api/order/my` | ✅ 完整 | 分页+角色+状态筛选 |

### 1.2 Service 层状态

| Service | 状态 | 说明 |
|---------|------|------|
| `OrderService` | ⚠️ 部分 | `create` 和 `getMyOrder` 可用；`update` 硬编码状态 |
| `PaymentService` | ❌ 空壳 | 无自定义方法 |
| `RefundService` | ❌ 空壳 | 无自定义方法 |
| `ReviewService` | ❌ 空壳 | 无自定义方法 |

### 1.3 前端状态

| 层 | 状态 |
|----|------|
| `api/order.js` | ❌ 只有 import，0 个函数 |
| 订单页面 | ❌ 无任何订单页面 |
| Detail.vue 购买按钮 | ❌ 无 click 事件 |
| Mine.vue 我买到的 | ❌ tab 存在但无内容 |

### 1.4 已有资源（可直接复用）

| 类别 | 内容 |
|------|------|
| 实体 | `Order`, `OrderAddress`, `Payment`, `Refund`, `Review`, `ReviewImage` 全部存在 |
| Mapper | 6 个 Mapper 全部存在（均继承 BaseMapper） |
| DTO/VO | `OrderDTO`, `OrderVO`, `MyOrderVO` 存在 |
| 数据库 | 6 张表已定义（order, order_address, order_payment, order_refund, order_review, order_review_image） |

---

## 二、订单状态流转

```
创建订单 → 0(待付款) → 1(待发货) → 2(待收货) → 3(已完成)
                  ↓                    ↓
               4(已取消)            5(退款中) → 3(已退款)
```

| 状态 | 含义 | 触发操作 |
|------|------|---------|
| 0 | 待付款 | `create` 创建订单 |
| 1 | 待发货 | `buy` 支付成功 |
| 2 | 待收货 | `send` 卖家发货 |
| 3 | 已完成 | `confirm` 买家确认收货 |
| 4 | 已取消 | `cancel` 买家取消订单 |
| 5 | 退款中 | `refund/apply` 买家申请退款 |

---

## 三、资金流转设计

```
买家余额 → 冻结金额 → 卖家余额
  (支付时)   (支付后)   (确认收货时)
```

| 阶段 | 操作 | 钱包流水 |
|------|------|---------|
| 支付（buy） | 买家余额 -price，冻结 +price | 买家 type=2(支出) businessType=2(消费) |
| 确认收货（confirm） | 卖家余额 +price，冻结 -price（卖家的） | 卖家 type=1(收入) businessType=2(消费) |
| 取消订单（cancel） | 买家冻结 -price，余额 +price | 买家 type=1(收入) businessType=3(退款) |

---

## 四、开发任务

### 阶段一：后端 Service 层补全

#### 4.1 OrderService 新增方法

```java
// 订单详情（含地址）
OrderDetailVO getDetail(Long orderId);

// 发货
void sendOrder(Long orderId, String deliveryNo);

// 确认收货（事务：改状态 + 卖家加钱 + 改商品 + 写流水）
void confirmOrder(Long orderId);

// 取消订单（事务：改状态 + 解冻买家余额 + 恢复商品 + 写流水）
void cancelOrder(Long orderId);
```

#### 4.2 PaymentService 新增方法

```java
// 创建支付记录
void createPayment(Long orderId, BigDecimal amount);

// 更新支付状态
void updateStatus(Long orderId, Integer status);
```

#### 4.3 RefundService 新增方法

```java
// 申请退款
void applyRefund(Long orderId, String reason, BigDecimal amount);
```

#### 4.4 新增 VO：OrderDetailVO

```java
// 订单详情（含地址信息）
public class OrderDetailVO {
    private Long id;
    private String orderNo;
    private Long buyerId;
    private Long sellerId;
    private Long goodsId;
    private String goodsTitle;
    private String goodsImage;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime payTime;
    private LocalDateTime sendTime;
    private LocalDateTime finishTime;
    private LocalDateTime createTime;
    // 收货地址
    private String receiver;
    private String phone;
    private String address;
}
```

#### 4.5 新增 DTO：RefundDTO

```java
public class RefundDTO {
    private Long orderId;
    private String reason;
    private BigDecimal amount;
}
```

### 阶段二：后端 Controller 层补全

#### 4.6 修复 create — 订单号 + 商品锁定

- 订单号改为 `时间戳 + 随机数`（如 `20260525143012001`）
- 创建订单时将商品状态 `1→3`（锁定），防止重复购买
- SQL 条件更新：`UPDATE goods SET status=3 WHERE id=? AND status=1`

#### 4.7 修复 buy — 支付记录 + 钱包流水

- 创建 `Payment` 记录（status=1 成功）
- 写买家钱包流水（type=2 支出，businessType=2 消费）
- 订单状态 `0→1`

#### 4.8 实现 orderDetail

- 查询 Order + OrderAddress + Goods 信息
- 组装 OrderDetailVO 返回

#### 4.9 实现 send

- 校验当前用户是卖家
- 订单状态 `1→2`，设置 sendTime
- 记录物流号（可存入 OrderAddress 或新增字段）

#### 4.10 实现 confirm（核心事务）

- 校验当前用户是买家
- 订单状态 `2→3`，设置 finishTime
- 卖家钱包：余额 +price
- 写卖家钱包流水（type=1 收入，businessType=2 消费）
- 商品状态 `3→2`（已卖出）

#### 4.11 实现 cancel

- 校验当前用户是买家，订单状态为 0
- 订单状态 `0→4`
- 解冻买家余额：冻结 -price，余额 +price
- 写买家钱包流水（type=1 收入，businessType=3 退款）
- 商品状态 `3→1`（恢复上架）

#### 4.12 实现 refund/apply

- 创建 Refund 记录（status=0 申请中）
- 订单状态 `2→5`（退款中）

### 阶段三：前端 API 层

#### 4.13 `api/order.js` 补全

```javascript
// 创建订单
export const createOrderApi = (data) => request.post('/order/create', data)

// 支付（购买）
export const buyOrderApi = (id) => request.post('/order/buy', null, { params: { id } })

// 订单详情
export const getOrderDetailApi = (id) => request.get(`/order/${id}`)

// 卖家发货
export const sendOrderApi = (id, deliveryNo) => request.post(`/order/${id}/send`, deliveryNo)

// 确认收货
export const confirmOrderApi = (id) => request.post(`/order/${id}/confirm`)

// 取消订单
export const cancelOrderApi = (id) => request.post(`/order/${id}/cancel`)

// 申请退款
export const applyRefundApi = (data) => request.post('/order/refund/apply', data)

// 我的订单列表
export const getMyOrderApi = (params) => request.get('/order/my', { params })
```

### 阶段四：前端页面

#### 4.14 新建 `OrderConfirm.vue` — 下单确认页

- 路由：`/order/confirm?goodsId=xxx`
- 显示：商品信息、价格、收货地址表单
- 提交：调用 `createOrderApi` → 调用 `buyOrderApi` → 跳转订单详情

#### 4.15 新建 `OrderDetail.vue` — 订单详情页

- 路由：`/order/detail?id=xxx`
- 显示：订单状态、商品信息、收货地址、操作按钮
- 按钮根据状态显示：
  - 待付款 → 去支付 / 取消订单
  - 待发货 → （等待卖家发货）
  - 待收货 → 确认收货 / 申请退款
  - 已完成 → 查看评价

#### 4.16 修改 `Detail.vue` — 购买按钮

- "立即购买" 按钮加 `@click` 跳转 `/order/confirm?goodsId=xxx`

#### 4.17 修改 `Mine.vue` — 订单 tab 内容

- "我买到的" tab：调用 `getMyOrderApi(role=1)` 显示订单列表
- "我卖出的" tab（改为"我卖出的"）：调用 `getMyOrderApi(role=2)` 显示订单列表
- 订单卡片：商品图+标题+价格+状态标签+操作按钮

#### 4.18 路由注册

```javascript
const OrderConfirm = () => import('../views/OrderConfirm.vue')
const OrderDetail = () => import('../views/OrderDetail.vue')

// Layout children 中增加：
{ path: 'order/confirm', component: OrderConfirm },
{ path: 'order/detail', component: OrderDetail },
```

### 阶段五：文档更新

#### 4.19 更新 `order-api.md`

- 移除所有 "桩代码" 警告
- 补充请求/响应示例
- 补充 OrderDetailVO 字段说明
- 补充 RefundDTO 字段说明

---

## 五、修改文件清单

### 后端（10 个文件）

| 文件 | 操作 | 说明 |
|------|------|------|
| `OrderService.java` | 修改 | 新增 4 个方法 |
| `OrderServiceImpl.java` | 修改 | 实现 4 个新方法 + 修复 create/update |
| `PaymentService.java` | 修改 | 新增 2 个方法 |
| `PaymentServiceImpl.java` | 修改 | 实现 2 个方法 |
| `RefundService.java` | 修改 | 新增 1 个方法 |
| `RefundServiceImpl.java` | 修改 | 实现 1 个方法 |
| `OrderController.java` | 修改 | 实现 5 个桩接口 + 修复 2 个接口 |
| `OrderDetailVO.java` | 新建 | 订单详情 VO |
| `RefundDTO.java` | 新建 | 退款请求 DTO |
| `order-api.md` | 修改 | 更新文档 |

### 前端（5 个文件）

| 文件 | 操作 | 说明 |
|------|------|------|
| `api/order.js` | 修改 | 补全 8 个 API 函数 |
| `views/OrderConfirm.vue` | 新建 | 下单确认页 |
| `views/OrderDetail.vue` | 新建 | 订单详情页 |
| `views/Detail.vue` | 修改 | 购买按钮加跳转 |
| `views/Mine.vue` | 修改 | "我买到的"tab 加内容 |
| `router/index.js` | 修改 | 加 2 个路由 |

---

## 六、订单状态字典

| 值 | 含义 | 前端标签颜色 |
|----|------|-------------|
| 0 | 待付款 | warning（橙） |
| 1 | 待发货 | primary（蓝） |
| 2 | 待收货 | success（绿） |
| 3 | 已完成 | info（灰） |
| 4 | 已取消 | danger（红） |
| 5 | 退款中 | danger（红） |

---

## 七、关键设计决策

1. **下单即锁定商品**：create 时商品状态 1→3，防止重复购买
2. **支付即冻结**：buy 时买家余额扣减、冻结增加，钱从买家"消失"
3. **确认才到账**：confirm 时卖家才收到钱，平台担保交易
4. **取消即解冻**：cancel 时冻结退回余额，商品恢复上架
5. **事务保证**：confirm/cancel 涉及多表操作，必须 `@Transactional`
6. **权限校验**：send 校验卖家身份，confirm/cancel 校验买家身份
