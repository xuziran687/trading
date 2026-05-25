# Longan Apex 接口文档

> 基于代码自动生成 — 与实际 Controller/DTO/VO 保持严格一致

---

## 目录

- [通用说明](#通用说明)
- [一、用户域 (User)](#一用户域-user)
- [二、商品域 (Goods)](#二商品域-goods)
- [三、订单域 (Order)](#三订单域-order)
- [四、聊天域 (Chat)](#四聊天域-chat)
- [五、公共域 (Common)](#五公共域-common)

---

## 通用说明

### 统一返回格式

```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

| 字段 | 说明 |
|------|------|
| `code` | 200 = 成功，500 = 服务器错误 |
| `msg` | 提示信息 |
| `data` | 业务数据（可能为 null） |

### 认证方式

```
Authorization: Bearer {jwt_token}
```

> 除 `/api/user/login`、`/api/user/register` 外，所有接口均需携带 Token。

### 分页参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `page` | Integer | 1 | 页码 |
| `size` | Integer | 10 | 每页条数 |

### 分页返回格式

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "total": 100,
    "records": []
  }
}
```

### 金额与时间格式

- 金额类型：`BigDecimal`（数据库 `DECIMAL(12,2)`）
- 时间格式：`yyyy-MM-dd HH:mm:ss`（JSON 序列化为 `LocalDateTime` / `Date` 默认格式）

---

## 一、用户域 (User)

### 1.1 用户注册

`POST /api/user/register`

**请求体**

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**逻辑说明**
1. 创建用户主表记录（ID 自动回填）
2. 初始化钱包（余额/冻结/累计收入/累计支出 均为 0）
3. （钱包流水、信用等由业务方自行扩展）

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 1.2 用户登录

`POST /api/user/login`

**请求体**

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**响应 — 成功**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "userId": 1,
    "nickname": "小明",
    "avatar": null,
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

> 登录失败时 `data` 为 `null`，`msg` 返回 "用户名或密码错误"。

**LoginVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `userId` | Long | 用户 ID |
| `nickname` | String | 昵称 |
| `avatar` | String | 头像 URL |
| `token` | String | JWT Token |

---

### 1.3 获取当前用户信息

`GET /api/user/info`

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "email": "user@example.com",
    "password": "$2a$10$...",
    "nickname": "小明",
    "avatar": null,
    "status": 1,
    "isDeleted": 0,
    "createTime": "2026-01-01T12:00:00",
    "updateTime": "2026-01-01T12:00:00"
  }
}
```

> 直接返回 `User` 实体全部字段（含密码等敏感字段，后续应改为 VO）。

---

### 1.4 更新用户信息

`PUT /api/user/info`

**请求体**

```json
{
  "username": "zhangsan_new",
  "nickname": "小明同学",
  "avatar": "http://example.com/avatar.png"
}
```

**UserInfoDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| `username` | String | 用户名 | 否 |
| `nickname` | String | 昵称 | 否 |
| `avatar` | String | 头像 URL | 否 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 1.5 获取用户资料

`GET /api/user/profile`

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "gender": 1,
    "birthday": "2000-01-01",
    "address": "北京市海淀区",
    "signature": "热爱生活",
    "createTime": "2026-01-01T12:00:00",
    "updateTime": "2026-01-01T12:00:00"
  }
}
```

> 直接返回 `UserProfile` 实体。无资料时 `data` 为 `null`。

---

### 1.6 设置用户资料

`POST /api/user/profile`

> 若用户尚无资料则新增，已有资料则自动转为更新。

**请求体**

```json
{
  "gender": 1,
  "birthday": "2000-01-01",
  "address": "北京市海淀区",
  "signature": "热爱生活"
}
```

**UserProfileDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| `gender` | Integer | 性别：0 未知 1 男 2 女 | 否 |
| `birthday` | LocalDate | 生日，格式 `yyyy-MM-dd` | 否 |
| `address` | String | 地址 | 否 |
| `signature` | String | 个性签名 | 否 |

---

### 1.7 更新用户资料

`PUT /api/user/profile`

**请求体** — 与 1.6 相同。

---

## 二、商品域 (Goods)

### 2.1 发布商品

`POST /api/goods`

**请求体**

```json
{
  "title": "iPhone 13 128G 蓝色",
  "description": "95新，无划痕，功能完好",
  "price": 3500.00,
  "originalPrice": 5999.00,
  "categoryId": 101,
  "quality": 3,
  "imageUrls": ["http://example.com/img1.png", "http://example.com/img2.png"]
}
```

**GoodsDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| `title` | String | 商品标题 | 是 |
| `description` | String | 商品描述 | 否 |
| `price` | BigDecimal | 价格 | 是 |
| `originalPrice` | BigDecimal | 原价 | 否 |
| `categoryId` | Long | 分类 ID | 是 |
| `quality` | Integer | 成色：1 全新 2 99新 3 95新 4 九成新 5 七成新及以下 | 否 |
| `imageUrls` | String[] | 商品图片 URL 数组 | 否 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 2.2 商品详情

`GET /api/goods/{id}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 商品 ID |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "title": "iPhone 13 128G 蓝色",
    "description": "95新，无划痕，功能完好",
    "price": 3500.00,
    "originalPrice": 5999.00,
    "categoryId": 101,
    "categoryName": "手机",
    "userId": 2,
    "nickname": "卖家小王",
    "avatar": "http://example.com/avatar.png",
    "quality": 3,
    "status": 1,
    "viewCount": 120,
    "collectCount": 15,
    "imageUrls": ["http://example.com/img1.png"],
    "updateTime": "2026-01-01T10:00:00"
  }
}
```

**GoodsDetailsVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `title` | String | 商品标题 |
| `description` | String | 商品描述 |
| `price` | BigDecimal | 价格 |
| `originalPrice` | BigDecimal | 原价 |
| `categoryId` | Long | 分类 ID |
| `categoryName` | String | 分类名称 |
| `userId` | Long | 卖家 ID |
| `nickname` | String | 卖家昵称 |
| `avatar` | String | 卖家头像 |
| `quality` | Integer | 成色等级 |
| `status` | Integer | 状态：0 下架 1 上架 2 已卖出 |
| `viewCount` | Integer | 浏览量 |
| `collectCount` | Integer | 收藏量 |
| `imageUrls` | String[] | 商品图片列表 |
| `updateTime` | LocalDateTime | 更新时间 |

---

### 2.3 商品列表（搜索/筛选）

`GET /api/goods/list`

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `keyword` | String | — | 关键词搜索（标题或描述） |
| `categoryId` | Long | — | 分类 ID |
| `minPrice` | BigDecimal | — | 最低价格 |
| `maxPrice` | BigDecimal | — | 最高价格 |
| `sort` | Integer | — | 排序：1 综合 2 最新 3 价格升序 4 价格降序 |
| `quality` | Integer | — | 成色：1-5 |
| `page` | Integer | 1 | 页码 |
| `size` | Integer | 10 | 每页条数 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1001,
        "title": "iPhone 13 128G 蓝色",
        "description": "95新，无划痕，功能完好",
        "price": 3500.00,
        "originalPrice": 5999.00,
        "categoryId": 101,
        "userId": 2,
        "quality": 3,
        "status": 1,
        "isDeleted": 0,
        "viewCount": 120,
        "collectCount": 15,
        "createTime": "2026-01-01T10:00:00",
        "updateTime": "2026-01-01T10:00:00"
      }
    ]
  }
}
```

> 返回 `Goods` 实体分页数据。

---

### 2.4 我的商品

`GET /api/goods/my`

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `page` | Integer | 1 | 页码 |
| `size` | Integer | 10 | 每页条数 |

> 当前无 `status` 参数过滤，返回当前用户所有商品。响应格式同 2.3。

---

### 2.5 修改商品

`PUT /api/goods/{id}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 商品 ID |

**请求体** — 同 2.1 GoodsDTO，仅更新传入的字段。

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 2.6 上下架商品

`POST /api/goods/{id}/status`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 商品 ID |

**查询参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `status` | Integer | 0 下架，1 上架 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 2.7 删除商品

`DELETE /api/goods/{id}`

> 逻辑删除，设置 `is_deleted = 1`。

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

---

### 2.8 分类 — 获取一级分类

`GET /api/category/level1`

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "name": "电子产品",
      "sort": 1
    },
    {
      "id": 2,
      "parentId": 0,
      "name": "服饰鞋包",
      "sort": 2
    }
  ]
}
```

**CategoryVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 分类 ID |
| `parentId` | Long | 父分类 ID（0 为顶级） |
| `name` | String | 分类名称 |
| `sort` | Integer | 排序值 |

---

### 2.9 分类 — 获取子分类

`GET /api/category/children/{parentId}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `parentId` | Long | 父分类 ID |

**响应** — 同 2.8 格式，返回指定父级下的子分类列表。

---

## 三、订单域 (Order)

### 3.1 创建订单

`POST /api/order/create`

**请求体**

```json
{
  "goodsId": 1001,
  "receiver": "小明",
  "phone": "13800138000",
  "address": "北京市海淀区"
}
```

**OrderDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| `goodsId` | Long | 商品 ID | 是 |
| `receiver` | String | 收货人 | 是 |
| `phone` | String | 联系电话 | 是 |
| `address` | String | 收货地址 | 是 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "id": 2001,
    "orderNo": "O202601010001",
    "price": 3500.00,
    "pointAmount": 3500.00
  }
}
```

**OrderVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |
| `orderNo` | String | 订单号 |
| `price` | BigDecimal | 商品价格 |
| `pointAmount` | BigDecimal | 平台币支付金额 |

---

### 3.2 购买商品（余额支付）

`POST /api/order/buy`

**查询参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |

**逻辑说明**
1. 获取订单信息（防止重复购买）
2. 更新订单状态
3. 校验买家钱包余额 ≥ 商品价格
4. 扣减余额，增加冻结金额

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

> 余额不足时：`{"code": 500, "msg": "余额不足，请先充值", "data": null}`

---

### 3.3 卖家发货

`POST /api/order/{id}/send`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |

**请求体**（纯文本）

```
SF1234567890
```

**响应**

```json
{
  "code": 200,
  "msg": "已标记为发货",
  "data": null
}
```

> ⚠️ 当前实现仅返回成功消息，未实际更新订单状态。

---

### 3.4 确认收货

`POST /api/order/{id}/confirm`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |

**响应**

```json
{
  "code": 200,
  "msg": "交易完成",
  "data": null
}
```

> ⚠️ 当前实现仅返回成功消息，转账逻辑待完善。

---

### 3.5 取消订单

`POST /api/order/{id}/cancel`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |

**响应**

```json
{
  "code": 200,
  "msg": "订单已取消",
  "data": null
}
```

> ⚠️ 当前实现仅返回成功消息，状态恢复逻辑待完善。

---

### 3.6 申请退款

`POST /api/order/refund/apply`

**请求体**（任意 JSON，当前未做反序列化）

```json
{
  "orderId": 2001,
  "reason": "不想要了",
  "amount": 3500.00
}
```

**响应**

```json
{
  "code": 200,
  "msg": "申请已提交",
  "data": null
}
```

> ⚠️ 当前为桩代码，未实现实际退款逻辑。

---

### 3.7 订单详情

`GET /api/order/{id}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 订单 ID |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": null
}
```

> ⚠️ 当前实现仅返回成功，未返回订单详情数据。

---

### 3.8 我的订单列表

`GET /api/order/my`

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `role` | Integer | — | 角色：1 买家 2 卖家 |
| `status` | Integer | — | 状态：0 待付款 1 待发货 2 待收货 3 已完成 4 已取消 |
| `page` | Integer | 1 | 页码 |
| `size` | Integer | 10 | 每页条数 |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "total": 10,
    "records": [
      {
        "orderId": 2001,
        "orderNo": "O202601010001",
        "goodsId": 1001,
        "title": "iPhone 13 128G 蓝色",
        "imageUrl": "http://example.com/img1.png",
        "price": 3500.00,
        "status": 0,
        "createTime": "2026-01-01T12:00:00"
      }
    ]
  }
}
```

**MyOrderVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `orderId` | Long | 订单 ID |
| `orderNo` | String | 订单编号 |
| `goodsId` | Long | 商品 ID |
| `title` | String | 商品标题 |
| `imageUrl` | String | 商品首图 |
| `price` | BigDecimal | 订单价格 |
| `status` | Integer | 0 待付款 1 待发货 2 待收货 3 已完成 4 已取消 |
| `createTime` | LocalDateTime | 创建时间 |

---

## 四、聊天域 (Chat)

> ⚠️ 聊天模块当前 **仅有 Service 层实现，未编写 Controller**。以下为基于 Service 接口设计的预期接口，实际以开发为准。

### 4.1 获取会话列表

`GET /api/chat/conversation/list`（预期）

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": [
    {
      "id": 3001,
      "userId": 1,
      "targetId": 2,
      "goodsId": 1001,
      "lastMsg": "好的，明天发货",
      "unread": 2,
      "updateTime": "2026-01-01T15:00:00"
    }
  ]
}
```

### 4.2 创建/获取会话

`POST /api/chat/conversation`（预期）

### 4.3 获取聊天记录

`GET /api/chat/message/list/{conversationId}`（预期）

### 4.4 发送消息

`POST /api/chat/message`（预期）

### 4.5 标记消息已读

`POST /api/chat/message/read/{conversationId}`（预期）

### 4.6 系统消息列表

`GET /api/chat/sys/message`（预期）

---

## 五、公共域 (Common)

### 5.1 文件上传

`POST /api/common/upload/{type}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `type` | String | 业务类型：`user`（用户头像）或 `goods`（商品图片） |

**请求格式**：`multipart/form-data`

| 字段 | 类型 | 说明 |
|------|------|------|
| `file` | MultipartFile | 上传文件（最大 10MB） |

**响应**

```json
{
  "code": 200,
  "msg": "成功",
  "data": "http://localhost:8088/images/goods/uuid-filename.png"
}
```

**返回 URL 格式**

```
{domain}/{type}/{uuid}.{ext}
# 示例：http://localhost:8088/images/user/a1b2c3d4.jpg
```

**错误处理**

| 条件 | 响应 |
|------|------|
| 文件为空 | `{"code": 500, "msg": "上传文件不能为空"}` |
| type 非 `user`/`goods` | `{"code": 500, "msg": "非法的上传类型"}` |

---

## 附录：数据字典

### 订单状态枚举

| 值 | 含义 |
|----|------|
| 0 | 待付款 |
| 1 | 待发货 |
| 2 | 待收货 |
| 3 | 已完成 |
| 4 | 已取消 |
| 5 | 退款中 |

### 商品状态枚举

| 值 | 含义 |
|----|------|
| 0 | 下架 |
| 1 | 上架 |
| 2 | 已卖出 |

### 商品成色枚举

| 值 | 含义 |
|----|------|
| 1 | 全新 |
| 2 | 99新 |
| 3 | 95新 |
| 4 | 九成新 |
| 5 | 七成新及以下 |

### 用户状态枚举

| 值 | 含义 |
|----|------|
| 0 | 禁用 |
| 1 | 正常 |

### 聊天消息类型

| 值 | 含义 |
|----|------|
| 1 | 文字 |
| 2 | 图片 |
| 3 | 系统消息 |

### 聊天消息状态

| 值 | 含义 |
|----|------|
| 0 | 未读 |
| 1 | 已读 |

### 系统消息类型

| 值 | 含义 |
|----|------|
| 1 | 订单 |
| 2 | 系统 |
| 3 | 活动 |
