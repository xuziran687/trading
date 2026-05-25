# 用户域接口 (User)

## 通用说明

**认证方式**：除注册/登录外，所有接口需携带 `Authorization: Bearer {jwt_token}`

**统一返回格式**：
```json
{"code": 200, "msg": "成功", "data": {}}
```

---

## 1. 用户注册

`POST /api/user/register`

**请求体**
```json
{"email": "user@example.com", "password": "123456"}
```

**逻辑说明**
1. 创建用户主表记录（ID 自动回填）
2. 初始化钱包（余额/冻结/累计收入/累计支出 均为 0）

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 2. 用户登录

`POST /api/user/login`

**请求体**
```json
{"email": "user@example.com", "password": "123456"}
```

**响应 — 成功**
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "userId": 1, "nickname": "小明", "avatar": null,
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

**LoginVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| userId | Long | 用户 ID |
| nickname | String | 昵称 |
| avatar | String | 头像 URL |
| token | String | JWT Token |

---

## 3. 获取当前用户信息

`GET /api/user/info`

**响应** — 返回 `User` 实体全部字段
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "id": 1, "username": "zhangsan", "email": "user@example.com",
    "nickname": "小明", "avatar": null, "status": 1,
    "isDeleted": 0, "createTime": "2026-01-01T12:00:00",
    "updateTime": "2026-01-01T12:00:00"
  }
}
```

---

## 4. 更新用户信息

`PUT /api/user/info`

**请求体**
```json
{"username": "zhangsan_new", "nickname": "小明同学", "avatar": "http://example.com/avatar.png"}
```

**UserInfoDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| username | String | 用户名 | 否 |
| nickname | String | 昵称 | 否 |
| avatar | String | 头像 URL | 否 |

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 5. 获取用户资料

`GET /api/user/profile`

**响应** — 返回 `UserProfile` 实体，无资料时 data 为 null
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "id": 1, "userId": 1, "gender": 1,
    "birthday": "2000-01-01", "address": "北京市海淀区",
    "signature": "热爱生活", "createTime": "2026-01-01T12:00:00",
    "updateTime": "2026-01-01T12:00:00"
  }
}
```

---

## 6. 设置用户资料

`POST /api/user/profile`

> 无资料则新增，已有资料自动转为更新。

**请求体**
```json
{"gender": 1, "birthday": "2000-01-01", "address": "北京市海淀区", "signature": "热爱生活"}
```

**UserProfileDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| gender | Integer | 性别：0 未知 1 男 2 女 | 否 |
| birthday | LocalDate | 生日 yyyy-MM-dd | 否 |
| address | String | 地址 | 否 |
| signature | String | 个性签名 | 否 |

---

## 7. 更新用户资料

`PUT /api/user/profile`

**请求体** — 同第 6 节。

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 8. 查询当前用户钱包

`GET /api/user/wallet`

> 返回余额、冻结金额、收支统计。

**响应**
```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "balance": 1250.80,
    "freeze": 200.00,
    "totalIncome": 5000.00,
    "totalOutcome": 3749.20
  }
}
```

**UserWalletVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| balance | BigDecimal | 可用余额 |
| freeze | BigDecimal | 冻结金额 |
| totalIncome | BigDecimal | 累计收入 |
| totalOutcome | BigDecimal | 累计支出 |

---

## 9. 查询钱包流水

`GET /api/user/wallet/log`

> 分页查询，支持按类型筛选。

**请求参数**

| 参数 | 必填 | 说明 |
|------|------|------|
| type | 否 | 1-收入 2-支出 |
| businessType | 否 | 1-充值 2-消费 3-退款 4-佣金 5-签到 6-活动 |
| page | 否 | 页码，默认 1 |
| size | 否 | 每页条数，默认 10 |

**响应**
```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "pages": 3,
    "total": 25,
    "list": [
      {
        "id": 1,
        "type": 2,
        "businessType": 2,
        "amount": 200.00,
        "balance": 1050.80,
        "orderNo": "20260525143012001",
        "remark": "购买商品：二手 MacBook",
        "createTime": "2026-05-25 14:30:12"
      }
    ]
  }
}
```

**WalletLogVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 流水 ID |
| type | Integer | 类型：1-收入 2-支出 |
| businessType | Integer | 业务类型：1-充值 2-消费 3-退款 4-佣金 5-签到 6-活动 |
| amount | BigDecimal | 变动金额 |
| balance | BigDecimal | 变动后余额 |
| orderNo | String | 关联订单号 |
| remark | String | 备注 |
| createTime | Date | 创建时间 |

---

## 10. 查询信用信息

`GET /api/user/credit`

**响应**
```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "score": 100,
    "level": 1,
    "goodNum": 0,
    "badNum": 0
  }
}
```

**UserCreditVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| score | Integer | 信用分 |
| level | Integer | 信用等级 |
| goodNum | Integer | 好评次数 |
| badNum | Integer | 差评次数 |

---

## 11. 记录用户行为

`POST /api/user/behavior`

> 浏览商品、搜索等场景调用。

**请求体**
```json
{
  "goodsId": 42,
  "categoryId": 5,
  "behavior": 1
}
```

**UserBehaviorDTO 字段**

| 字段 | 类型 | 说明 | 必填 |
|------|------|------|------|
| goodsId | Long | 商品 ID | 是 |
| categoryId | Long | 分类 ID | 否 |
| behavior | Integer | 行为类型：1-浏览 2-收藏 3-加购 4-下单 5-搜索 | 是 |

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```
