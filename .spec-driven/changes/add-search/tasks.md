# Tasks: add-search

## Implementation

- [ ] 在 `petstore-service/sql/services.sql` 末尾追加 `search_service` 的 `drop` + `create service` 声明（两个方法：`search_products`、`search_items`）
- [ ] 创建 `petstore-service/src/main/java/com/lealone/examples/petstore/service/SearchService.java`，实现 `searchProducts(String keyword)` 方法
- [ ] 在 `SearchService` 中实现 `searchItems(String keyword)` 方法
- [ ] 在 `PetStore.java` 的 `LealoneApplication.start(...)` 调用中追加 `services.sql` 路径（如果尚未包含）

## Verification

- [ ] `mvn compile` 编译通过，无错误
- [ ] 启动应用后访问 `/service/search_service/searchProducts?keyword=fish`，返回 JSON 且包含鱼类商品
- [ ] 访问 `/service/search_service/searchItems?keyword=large`，返回包含 `attr1=Large` 的 Item
- [ ] 关键词大小写不敏感：`keyword=FISH` 与 `keyword=fish` 返回相同结果
- [ ] 无匹配时返回空数组（`{"products":[]}`），而非报错
- [ ] Update specs in .spec-driven/specs/ if behavior changed
