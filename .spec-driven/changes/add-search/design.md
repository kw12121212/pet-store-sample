# Design: add-search

## Approach

遵循现有的 Lealone 三层模式：SQL 声明服务 → Java 实现 → 自动暴露 HTTP。

**新增文件：**
- `petstore-service/src/main/java/.../service/SearchService.java` — 搜索实现
- `petstore-service/sql/services.sql` — 追加 `search_service` 声明

**SearchService 实现思路：**

```java
// searchProducts: 用 Lealone ORM like() 查询，left join category
public String searchProducts(String keyword) {
    String pattern = "%" + keyword + "%";
    Category c = Category.dao;
    List<Product> list = Product.dao
        .leftJoin(c).on().categoryid.eq(c.catid)
        .where().name.like(pattern).or().descn.like(pattern)
        .findList();
    return new JsonObject().put("products", new JsonArray(list)).encode();
}

// searchItems: 搜索 item.attr1，left join product
public String searchItems(String keyword) {
    String pattern = "%" + keyword + "%";
    Product p = Product.dao;
    List<Item> list = Item.dao
        .leftJoin(p).on().productid.eq(p.productid)
        .where().attr1.like(pattern)
        .findList();
    return new JsonObject().put("items", new JsonArray(list)).encode();
}
```

**services.sql 追加：**
```sql
drop service if exists search_service;
create service if not exists search_service (
  search_products(keyword varchar) varchar,
  search_items(keyword varchar) varchar
) implement by 'com.lealone.examples.petstore.service.SearchService';
```

## Key Decisions

**使用数据库 LIKE 而非内存过滤**
Lealone ORM 原生支持 `like()`，直接下推到 SQL，无需将数据全量加载到内存再过滤。样本数据量小，性能足够。

**搜索范围限定 product 和 item 两个维度**
- `product` 是用户感知的"商品"概念，搜索 `name` 和 `descn` 最直观
- `item` 的 `attr1` 存储规格描述（如 "Large"、"Adult Male"），是用户筛选的关键词

**`PetStore.java` 中追加 services.sql 路径**
`LealoneApplication.start(...)` 接受可变参数的 SQL 文件列表，直接追加即可，无需修改启动逻辑。

## Alternatives Considered

**在 StoreService 中直接加搜索方法**
优点：不新增类。缺点：StoreService 职责会变宽，搜索是独立关注点，单独成类更清晰。

**在 product 搜索中同时匹配 item.attr1**
会产生多对多 join，结果结构复杂，不如分两个端点各自清晰。
