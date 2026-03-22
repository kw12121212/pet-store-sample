# Proposal: add-search

## What

为 Pet Store 增加搜索功能，允许用户通过关键词搜索商品（Product）和商品条目（Item）。

具体行为：
- 新增 HTTP 端点 `GET /service/search_service/searchProducts?keyword=xxx`，返回名称或描述中包含关键词的商品列表，含所属分类信息
- 新增 HTTP 端点 `GET /service/search_service/searchItems?keyword=xxx`，返回名称或属性（attr1）中包含关键词的 Item 列表，含商品名称和价格
- 搜索不区分大小写，结果以 JSON 格式返回

## Why

现有 API 只支持按 `productId` 精确查询，用户无法通过关键词发现商品。搜索是电商应用的核心功能，也是展示 Lealone ORM `like` 查询能力的典型示例。

## Scope

**In scope**
- 基于数据库 `LIKE` 查询的关键词搜索
- 搜索 `product.name`、`product.descn`
- 搜索 `item.attr1`（商品规格描述，如 "Large"、"Adult Male"）
- 搜索结果包含关联数据（Product 关联 Category，Item 关联 Product）
- 在 `services.sql` 中声明新服务

**Out of scope**
- 全文检索引擎（Elasticsearch 等）
- 搜索结果分页
- 搜索历史、热门搜索统计
- 前端 UI

## Unchanged Behavior

- 现有所有端点（`/service/store_service/` 等）行为不变
- 现有数据库表结构和数据不受影响
- 应用启动流程不变
