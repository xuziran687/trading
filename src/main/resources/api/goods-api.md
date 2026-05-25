# 商品域接口 (Goods)

包含商品管理 + 分类管理。

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

## 一、商品管理

### 1. 发布商品

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
| title | String | 商品标题 | 是 |
| description | String | 商品描述 | 否 |
| price | BigDecimal | 价格 | 是 |
| originalPrice | BigDecimal | 原价 | 否 |
| categoryId | Long | 分类 ID | 是 |
| quality | Integer | 成色：1 全新 2 99新 3 95新 4 九成新 5 七成新及以下 | 否 |
| imageUrls | String[] | 商品图片 URL 数组 | 否 |

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

### 2. 商品详情

`GET /api/goods/{id}`

**响应**
```json
{
  "code": 200, "msg": "成功",
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
| title | String | 商品标题 |
| description | String | 商品描述 |
| price | BigDecimal | 价格 |
| originalPrice | BigDecimal | 原价 |
| categoryId | Long | 分类 ID |
| categoryName | String | 分类名称 |
| userId | Long | 卖家 ID |
| nickname | String | 卖家昵称 |
| avatar | String | 卖家头像 |
| quality | Integer | 成色等级 |
| status | Integer | 0 下架 1 上架 2 已卖出 |
| viewCount | Integer | 浏览量 |
| collectCount | Integer | 收藏量 |
| imageUrls | String[] | 商品图片列表 |
| updateTime | LocalDateTime | 更新时间 |

---

### 3. 商品列表（搜索/筛选）

`GET /api/goods/list`

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| keyword | String | — | 关键词搜索 |
| categoryId | Long | — | 分类 ID |
| minPrice | BigDecimal | — | 最低价格 |
| maxPrice | BigDecimal | — | 最高价格 |
| sort | Integer | — | 1 综合 2 最新 3 价格升序 4 价格降序 |
| quality | Integer | — | 成色 1-5 |
| page | Integer | 1 | 页码 |
| size | Integer | 10 | 每页条数 |

**响应** — 返回 `Goods` 实体分页
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "total": 100,
    "records": [{
      "id": 1001, "title": "iPhone 13 128G 蓝色",
      "description": "95新，无划痕，功能完好",
      "price": 3500.00, "originalPrice": 5999.00,
      "categoryId": 101, "userId": 2,
      "quality": 3, "status": 1, "isDeleted": 0,
      "viewCount": 120, "collectCount": 15,
      "createTime": "2026-01-01T10:00:00",
      "updateTime": "2026-01-01T10:00:00"
    }]
  }
}
```

---

### 4. 我的商品

`GET /api/goods/my`

**查询参数**：page（默认 1）、size（默认 10）

**响应** — 同第 3 节格式，返回当前用户的商品。

---

### 5. 修改商品

`PUT /api/goods/{id}`

**请求体** — 同第 1 节 GoodsDTO

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

### 6. 上下架商品

`POST /api/goods/{id}/status`

**查询参数**：`status` — 0 下架，1 上架

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

### 7. 删除商品

`DELETE /api/goods/{id}`

> 逻辑删除，设置 `is_deleted = 1`。

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 二、分类管理

### 8. 获取一级分类

`GET /api/category/level1`

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": [
    {"id": 1, "parentId": 0, "name": "电子产品", "sort": 1},
    {"id": 2, "parentId": 0, "name": "服饰鞋包", "sort": 2}
  ]
}
```

**CategoryVO 字段**：id / parentId / name / sort

---

### 9. 获取子分类

`GET /api/category/children/{parentId}`

**响应** — 同第 8 节格式，返回指定父级下的子分类列表。

---

## 附录：商品数据字典

### 商品状态

| 值 | 含义 |
|----|------|
| 0 | 下架 |
| 1 | 上架 |
| 2 | 已卖出 |

### 商品成色

| 值 | 含义 |
|----|------|
| 1 | 全新 |
| 2 | 99新 |
| 3 | 95新 |
| 4 | 九成新 |
| 5 | 七成新及以下 |
