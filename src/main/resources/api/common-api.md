# 公共域接口 (Common)

## 通用说明

**统一返回格式**：
```json
{"code": 200, "msg": "成功", "data": {}}
```

---

## 1. 文件上传

`POST /api/common/upload/{type}`

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| type | String | 业务类型：`user`（用户头像）或 `goods`（商品图片） |

**请求格式**：`multipart/form-data`

| 字段 | 类型 | 说明 |
|------|------|------|
| file | MultipartFile | 上传文件（最大 10MB） |

**存储路径**

```
{basePath}/{type}/{uuid}.{ext}
# 示例：/opt/longan/images/goods/a1b2c3d4.png
```

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": "http://localhost:8088/images/goods/a1b2c3d4.png"
}
```

**返回 URL 格式**
```
{domain}/{type}/{uuid}.{ext}
```

**错误处理**

| 条件 | 响应 msg |
|------|----------|
| 文件为空 | 上传文件不能为空 |
| type 非 user/goods | 非法的上传类型 |

---

## 2. （规划）平台配置

`GET /api/common/config/list`（预期）

> 当前 `PlatformConfigService` 已实现，Controller 路由待补充。

**响应**
```json
{
  "code": 200, "msg": "成功",
  "data": [
    {"id": 1, "configKey": "pointRate", "configValue": "10", "desc": "平台币汇率"}
  ]
}
```
