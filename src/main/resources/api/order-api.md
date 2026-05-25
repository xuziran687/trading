# 订单域接口 (Order)

## 通用说明

**统一返回格式**：
```json
{"code": 200, "msg": "成功", "data": {}}
```

**分页返回格式**：
```json
{"code": 200, "msg": "成功", "data": {"total": 100, "records": []}}
```

---

## 1. 创建订单

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
| goodsId | Long | 商品 ID | 是 |
| receiver | String | 收货人 | 是 |
| phone | String | 联系电话 | 是 |
| address | String | 收货地址 | 是 |

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "id": 2001, "orderNo": "O202601010001",
    "price": 3500.00, "pointAmount": 3500.00
  }
}
```

**OrderVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 订单 ID |
| orderNo | String | 订单号 |
| price | BigDecimal | 商品价格 |
| pointAmount | BigDecimal | 平台币支付金额 |

---

## 2. 购买商品（余额支付）

`POST /api/order/buy`

**查询参数**：`id` — 订单 ID

**逻辑**
1. 获取订单信息
2. 更新订单状态
3. 校验余额 ≥ 价格，扣减余额、增加冻结

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

> 余额不足时：`{"code": 500, "msg": "余额不足，请先充值"}`

---

## 3. 卖家发货

`POST /api/order/{id}/send`

**请求体**（纯文本）：`SF1234567890`

**响应**
```json
{"code": 200, "msg": "已标记为发货", "data": null}
```

> ⚠️ 当前为桩代码，未实际更新订单状态。

---

## 4. 确认收货

`POST /api/order/{id}/confirm`

**响应**
```json
{"code": 200, "msg": "交易完成", "data": null}
```

> ⚠️ 当前为桩代码，转账逻辑待完善。

---

## 5. 取消订单

`POST /api/order/{id}/cancel`

**响应**
```json
{"code": 200, "msg": "订单已取消", "data": null}
```

> ⚠️ 当前为桩代码，状态恢复逻辑待完善。

---

## 6. 申请退款

`POST /api/order/refund/apply`

**请求体**
```json
{"orderId": 2001, "reason": "不想要了", "amount": 3500.00}
```

**响应**
```json
{"code": 200, "msg": "申请已提交", "data": null}
```

> ⚠️ 当前为桩代码，未实现实际退款逻辑。

---

## 7. 订单详情

`GET /api/order/{id}`

> ⚠️ 当前实现仅返回成功，未返回订单详情数据。

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 8. 我的订单列表

`GET /api/order/my`

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| role | Integer | — | 1 买家 2 卖家 |
| status | Integer | — | 0 待付款 1 待发货 2 待收货 3 已完成 4 已取消 |
| page | Integer | 1 | 页码 |
| size | Integer | 10 | 每页条数 |

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "total": 10,
    "records": [{
      "orderId": 2001, "orderNo": "O202601010001",
      "goodsId": 1001, "title": "iPhone 13 128G 蓝色",
      "imageUrl": "http://example.com/img1.png",
      "price": 3500.00, "status": 0,
      "createTime": "2026-01-01T12:00:00"
    }]
  }
}
```

**MyOrderVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| orderId | Long | 订单 ID |
| orderNo | String | 订单编号 |
| goodsId | Long | 商品 ID |
| title | String | 商品标题 |
| imageUrl | String | 商品首图 |
| price | BigDecimal | 订单价格 |
| status | Integer | 0 待付款 1 待发货 2 待收货 3 已完成 4 已取消 |
| createTime | LocalDateTime | 创建时间 |

---

## 附录：订单状态字典

| 值 | 含义 |
|----|------|
| 0 | 待付款 |
| 1 | 待发货 |
| 2 | 待收货 |
| 3 | 已完成 |
| 4 | 已取消 |
| 5 | 退款中 |
