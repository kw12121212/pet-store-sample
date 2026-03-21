# AGENTS.md

Instructions for AI agents working in this repository.

## What This Repo Is

A classic **Pet Store** backend built with [Lealone-Platform](https://github.com/lealone/Lealone-Platform) — a high-concurrency Java application framework that auto-generates ORM model classes from SQL schema and exposes service methods as HTTP endpoints.

The project is adapted from the [Webx PetStore](https://github.com/webx/citrus-sample), covering a full catalog (categories → products → items), user accounts, and a shopping cart.

## Architecture

```
pet-store-sample/
├── petstore-model/          # ORM model classes + SQL schema
│   ├── sql/
│   │   ├── tables.sql       # DDL for 13 tables + code-gen directives
│   │   └── init-data.sql    # Sample data (fish, dogs, cats, reptiles, birds)
│   └── src/main/java/.../model/
│       ├── Category, Product, Item        # Catalog
│       ├── User, Account, Profile         # Users
│       ├── Orders, OrderItem, OrderStatus # Orders
│       ├── Inventory, Supplier            # Stock
│       └── BannerData, Sequence           # Misc
├── petstore-service/        # Service implementations
│   ├── sql/services.sql     # HTTP service declarations
│   └── src/main/java/.../service/
│       ├── UserService      # login, logout, register, update, getUser
│       ├── StoreService     # getAllCategories, getAllProductItems, addProduct
│       └── ViewCartService  # addItem, removeItem, update, getItems
└── petstore-main/           # Application entry point
    └── src/main/java/.../main/PetStore.java
```

## Key Concepts

### Lealone ORM

Model classes extend `Model<T>` and use typed property objects for fluent, type-safe queries:

```java
// Insert
new User().userId.set("alice").password.set("secret").insert();

// Query
User user = User.dao.where().userId.eq("alice").findOne();

// Join
Product product = Product.dao
    .leftJoin(Category.dao).on().categoryid.eq(Category.dao.catid)
    .where().productid.eq("FI-SW-01").findOne();
```

Property types: `PString`, `PInteger`, `PBigDecimal`, `PDate`.

### Lealone Services

Services are declared in SQL, implemented as plain Java classes, and automatically exposed as HTTP endpoints:

```sql
create service if not exists store_service (
  get_all_categories() varchar
) implement by 'com.lealone.examples.petstore.service.StoreService';
```

Endpoint: `GET /service/store_service/getAllCategories`

Method name convention: SQL `get_all_categories` → Java `getAllCategories` → URL `/getAllCategories`.

### Model Code Generation

`tables.sql` contains Lealone-specific directives:

```sql
set @packageName 'com.lealone.examples.petstore.model';
set @srcPath '../petstore-model/src/main/java';

create table if not exists category ( ... )
package @packageName generate code @srcPath;
```

When this SQL runs, Lealone generates model Java source files at `@srcPath`. The files in `petstore-model/src/` are pre-generated and checked in — **do not edit them by hand**. Regenerate by running the app with fresh SQL if the schema changes.

## Build & Run

**Prerequisites:** JDK 21+, Maven 3.9+. The Lealone SNAPSHOT dependencies must be installed in the local Maven repository (see Setup below).

```bash
# Compile
mvn compile

# Package (creates petstore-main/target/petstore-1.0.0-all.jar)
mvn package -DskipTests

# Run
cd petstore-main
java -jar target/petstore-1.0.0-all.jar
```

App starts on `http://localhost:8080`.

## Test Endpoints

```
GET /service/user_service/login?userId=admin&password=admin
GET /service/user_service/register?userId=bob&password=123&password2=123
GET /service/store_service/getAllCategories
GET /service/store_service/getAllProductItems?productId=FI-SW-01
GET /service/view_cart_service/addItem?cartId=cart1&itemId=EST-1
GET /service/view_cart_service/getItems?cartId=cart1
```

Default users in `init-data.sql`: `admin/admin`, `j2ee/j2ee`.

## Setup: Installing Lealone Dependencies

The framework uses `8.0.0-SNAPSHOT` artifacts not yet on Maven Central. Build from source once:

```bash
# 1. Build Lealone core
cd ~/Code/Lealone
mvn install -DskipTests -q

# 2. Build Lealone-Platform
cd ~/Code/Lealone-Platform
mvn install -DskipTests -q -pl lealone-orm,lealone-service,lealone-tomcat,lealone-boot -am
```

If you see a `maven-shade-plugin 2.3` error with Maven 3.9+, upgrade the version in the affected `pom.xml`:

```xml
<!-- change 2.3 → 3.5.1 -->
<artifactId>maven-shade-plugin</artifactId>
<version>3.5.1</version>
```

## Module Dependencies

```
petstore-main → petstore-service → petstore-model → lealone-boot
```

`lealone-boot` transitively pulls in `lealone-orm`, `lealone-service`, and `lealone-tomcat`.

## Adding a New Feature

1. **Schema change** — edit `petstore-model/sql/tables.sql`, update the corresponding model class in `petstore-model/src/`.
2. **New service method** — add the signature to `petstore-service/sql/services.sql`, implement it in the relevant `*Service.java`.
3. **New service** — add a new `create service` block to `services.sql` and create the Java implementation class.
4. **No framework config** — there is no XML/YAML config. All wiring happens through SQL declarations and `LealoneApplication.start(...)`.

## Spec-Driven Workflow

This project has [slim-spec-driven](https://github.com/kw12121212/slim-spec-driven) installed. Use these skills for structured changes:

```
/spec-driven-propose <name>   # scaffold proposal + design + tasks
/spec-driven-apply            # implement tasks one by one
/spec-driven-verify           # check completion
/spec-driven-archive          # move to archive when done
```

Skills are symlinked from `~/Code/slim-spec-driven/`. On a new machine, re-run:

```bash
bash ~/Code/slim-spec-driven/install.sh --cli claude --project .
```

## Conventions

- **Package root:** `com.lealone.examples.petstore`
- **Model classes:** `*.model` package, named after the table (PascalCase)
- **Service classes:** `*.service` package, named `<Domain>Service`
- **SQL files:** `petstore-model/sql/` for DDL/data, `petstore-service/sql/` for service declarations
- **No test module yet** — add a `petstore-test` module with JUnit if tests are needed
- **Java source level:** 21 (compiled with JDK 25)
