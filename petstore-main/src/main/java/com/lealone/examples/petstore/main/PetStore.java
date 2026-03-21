package com.lealone.examples.petstore.main;

import com.lealone.plugins.boot.LealoneApplication;

public class PetStore {

    public static void main(String[] args) {
        // 启动应用，加载数据库表结构和初始数据，注册服务
        // 在浏览器中打开 http://localhost:8080/ 进行测试
        //
        // 测试 API 示例:
        // http://localhost:8080/service/store_service/getAllCategories
        // http://localhost:8080/service/store_service/getAllProductItems?productId=FI-SW-01
        // http://localhost:8080/service/user_service/login?userId=admin&password=admin
        // http://localhost:8080/service/view_cart_service/getItems?cartId=cart1
        LealoneApplication.start("petstore",
                "../petstore-model/sql/tables.sql",
                "../petstore-model/sql/init-data.sql",
                "../petstore-service/sql/services.sql");
    }
}
