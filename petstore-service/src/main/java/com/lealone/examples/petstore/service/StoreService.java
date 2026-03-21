package com.lealone.examples.petstore.service;

import java.util.List;

import com.lealone.examples.petstore.model.Category;
import com.lealone.examples.petstore.model.Item;
import com.lealone.examples.petstore.model.Product;
import com.lealone.plugins.orm.json.JsonArray;
import com.lealone.plugins.orm.json.JsonObject;

public class StoreService {

    public String addProduct(Product product) {
        product.insert();
        return null;
    }

    public String getAllCategories() {
        Product p = Product.dao;
        List<Category> list = Category.dao.join(p).on().catid.eq(p.categoryid) //
                .orderBy().catid.asc().findList();
        return new JsonObject().put("categories", new JsonArray(list)).encode();
    }

    public String getAllProductItems(String productId) {
        JsonObject json = new JsonObject();

        Category c = Category.dao;
        Product p = Product.dao;
        Item i = Item.dao;

        Product product = p.leftJoin(c).on().categoryid.eq(c.catid).leftJoin(i).on().productid
                .eq(i.productid) //
                .where().productid.eq(productId).findOne();

        json.put("category", product.getCategory());
        json.put("product", product);
        json.put("items", product.getItemList());

        return json.encode();
    }
}
