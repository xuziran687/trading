# 聊天域接口 (Chat)

> ⚠️ 聊天模块当前 **仅有 Service 层实现，未编写 Controller**。以下为基于 Service 接口设计的预期接口，实际以开发为准。

## 通用说明

**统一返回格式**：
```json
{"code": 200, "msg": "成功", "data": {}}
```

---

## 1. 获取会话列表

`GET /api/chat/conversation/list`（预期）

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": [{
    "id": 3001, "userId": 1, "targetId": 2,
    "goodsId": 1001, "lastMsg": "好的，明天发货",
    "unread": 2, "updateTime": "2026-01-01T15:00:00"
  }]
}
```

**ChatConversation 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 会话 ID |
| userId | Long | 当前用户 ID |
| targetId | Long | 对方用户 ID |
| goodsId | Long | 关联商品 ID |
| lastMsg | String | 最后一条消息 |
| unread | Integer | 未读消息数 |
| updateTime | Date | 更新时间 |

---

## 2. 创建/获取会话

`POST /api/chat/conversation`（预期）

**请求体**
```json
{"targetId": 2, "goodsId": 1001}
```

**响应**
```json
{"code": 200, "msg": "成功", "data": {"conversationId": 3001}}
```

---

## 3. 获取聊天记录

`GET /api/chat/message/list/{conversationId}`（预期）

**查询参数**：page（默认 1）、size（默认 20）

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "records": [{
      "id": 4001, "senderId": 1, "receiverId": 2,
      "goodsId": 1001, "content": "你好，商品还在吗？",
      "type": 1, "status": 0,
      "createTime": "2026-01-01T14:00:00"
    }]
  }
}
```

**ChatMessage 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 消息 ID |
| senderId | Long | 发送者 ID |
| receiverId | Long | 接收者 ID |
| goodsId | Long | 关联商品 ID |
| content | String | 消息内容 |
| type | Integer | 1 文字 2 图片 3 系统消息 |
| status | Integer | 0 未读 1 已读 |
| createTime | Date | 发送时间 |

---

## 4. 发送消息

`POST /api/chat/message`（预期）

**请求体**
```json
{
  "conversationId": 3001, "targetId": 2,
  "goodsId": 1001, "content": "你好，商品还在吗？",
  "type": 1
}
```

---

## 5. 标记消息已读

`POST /api/chat/message/read/{conversationId}`（预期）

**响应**
```json
{"code": 200, "msg": "成功", "data": null}
```

---

## 6. 系统消息列表

`GET /api/chat/sys/message`（预期）

**查询参数**：page（默认 1）、size（默认 10）

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": {
    "records": [{
      "id": 5001, "userId": 1, "title": "订单通知",
      "content": "您的订单已发货", "type": 1,
      "isRead": 0, "createTime": "2026-01-01T16:00:00"
    }]
  }
}
```

**SysMessage 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 消息 ID |
| userId | Long | 用户 ID |
| title | String | 标题 |
| content | String | 内容 |
| type | Integer | 1 订单 2 系统 3 活动 |
| isRead | Integer | 0 未读 1 已读 |
| createTime | Date | 发送时间 |

---

## 附录：聊天数据字典

### 消息类型

| 值 | 含义 |
|----|------|
| 1 | 文字 |
| 2 | 图片 |
| 3 | 系统消息 |

### 消息状态

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
