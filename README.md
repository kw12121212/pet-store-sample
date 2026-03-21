# Pet Store

基于 [Lealone-Platform](https://github.com/lealone/Lealone-Platform) 开发的经典宠物商店后端应用，改编自 [Webx PetStore](https://github.com/webx/citrus-sample)。

Lealone 是一个高并发 Java 应用框架，核心特点是：**用 SQL 定义表结构和服务接口，框架自动生成 ORM 模型类，并将服务方法直接暴露为 HTTP 端点**，无需 XML 配置、无需注解、无需 Controller。

---

## 功能

- 商品目录：分类（鱼 / 狗 / 猫 / 爬行动物 / 鸟）、商品、库存
- 用户系统：注册、登录、账户信息管理
- 购物车：添加/移除商品、查看购物车（基于内存）
- RESTful HTTP 服务，浏览器直接访问即可测试

---

## 环境要求

| 工具 | 版本 |
|------|------|
| JDK  | 21+  |
| Maven | 3.9+ |

---

## 快速开始

### 第一步：安装 Lealone 依赖

框架目前使用 `8.0.0-SNAPSHOT`，需要从源码构建并安装到本地 Maven 仓库：

```bash
# 克隆并安装 Lealone 核心库
git clone https://github.com/lealone/Lealone.git ~/Code/Lealone
cd ~/Code/Lealone
mvn install -DskipTests -q

# 克隆并安装 Lealone-Platform
git clone https://github.com/lealone/Lealone-Platform.git ~/Code/Lealone-Platform
cd ~/Code/Lealone-Platform
mvn install -DskipTests -q -pl lealone-orm,lealone-service,lealone-tomcat,lealone-boot -am
```

> **注意（Maven 3.9+）：** 如果出现 `maven-shade-plugin 2.3` 相关报错，将受影响 `pom.xml` 中的版本从 `2.3` 改为 `3.5.1` 即可。

### 第二步：构建项目

```bash
git clone https://github.com/kw12121212/pet-store-sample.git
cd pet-store-sample
mvn package -DskipTests
```

### 第三步：启动应用

```bash
cd petstore-main
java -jar target/petstore-1.0.0-all.jar
```

启动后访问 [http://localhost:8080](http://localhost:8080)

---

## API 测试

应用启动后，在浏览器或 curl 中直接访问：

**用户**
```
# 登录
http://localhost:8080/service/user_service/login?userId=admin&password=admin

# 注册新用户
http://localhost:8080/service/user_service/register?userId=bob&password=123&password2=123
```

**商品目录**
```
# 获取所有分类（含各分类下的商品列表）
http://localhost:8080/service/store_service/getAllCategories

# 查看某商品的详情和库存
http://localhost:8080/service/store_service/getAllProductItems?productId=FI-SW-01
```

**购物车**
```
# 添加商品
http://localhost:8080/service/view_cart_service/addItem?cartId=cart1&itemId=EST-1

# 查看购物车
http://localhost:8080/service/view_cart_service/getItems?cartId=cart1

# 移除商品
http://localhost:8080/service/view_cart_service/removeItem?cartId=cart1&itemId=EST-1
```

初始账号：`admin / admin`、`j2ee / j2ee`

---

## 项目结构

```
pet-store-sample/
├── petstore-model/          # 数据层
│   ├── sql/
│   │   ├── tables.sql       # 建表 SQL（13 张表）
│   │   └── init-data.sql    # 初始化数据
│   └── src/.../model/       # ORM 模型类（由框架自动生成）
├── petstore-service/        # 服务层
│   ├── sql/services.sql     # 服务接口声明
│   └── src/.../service/     # 服务实现
│       ├── UserService.java
│       ├── StoreService.java
│       └── ViewCartService.java
└── petstore-main/           # 启动入口
    └── src/.../PetStore.java
```

---

## Lealone 开发方式简介

传统框架需要写 DAO、Controller、路由配置。Lealone 的方式更简洁：

**1. 在 SQL 中定义表，框架生成模型类**

```sql
create table if not exists category (
    catid varchar(10) not null primary key,
    name  varchar(80),
    descn varchar(255)
) package 'com.lealone.examples.petstore.model' generate code './src/main/java';
```

**2. 用生成的模型类做 CRUD**

```java
// 插入
new User().userId.set("alice").password.set("secret").insert();

// 查询
User user = User.dao.where().userId.eq("alice").findOne();

// 联表查询（一次数据库访问）
Product product = Product.dao
    .leftJoin(Category.dao).on().categoryid.eq(Category.dao.catid)
    .where().productid.eq("FI-SW-01")
    .findOne();
```

**3. 在 SQL 中声明服务，写 Java 实现，自动变成 HTTP 接口**

```sql
create service if not exists store_service (
    get_all_categories() varchar
) implement by 'com.lealone.examples.petstore.service.StoreService';
```

```java
public class StoreService {
    public String getAllCategories() {
        // 业务逻辑
    }
}
```

访问：`GET /service/store_service/getAllCategories`

---

## 数据说明

初始数据包含：

| 分类 | 商品示例 |
|------|---------|
| FISH（鱼）| Angelfish、Tiger Shark、Koi、Goldfish |
| DOGS（狗）| Bulldog、Poodle、Labrador、Chihuahua |
| CATS（猫）| Manx、Persian |
| REPTILES（爬行动物）| Rattlesnake、Iguana |
| BIRDS（鸟）| Amazon Parrot、Finch |

共 16 种商品、28 条库存记录、2 个供应商。

---

## License

MIT
