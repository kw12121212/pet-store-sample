package com.lealone.examples.petstore.service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lealone.examples.petstore.model.Item;
import com.lealone.plugins.orm.json.JsonArray;
import com.lealone.plugins.orm.json.JsonObject;

public class ViewCartService {

    public static class ViewCart {
        CopyOnWriteArrayList<String> items = new CopyOnWriteArrayList<>();
    }

    private final ConcurrentHashMap<String, ViewCart> viewCarts = new ConcurrentHashMap<>();

    public void addItem(String cartId, String itemId) {
        ViewCart viewCart = new ViewCart();
        ViewCart old = viewCarts.putIfAbsent(cartId, viewCart);
        if (old != null)
            viewCart = old;
        viewCart.items.add(itemId);
    }

    public void removeItem(String cartId, String itemId) {
        ViewCart viewCart = viewCarts.get(cartId);
        if (viewCart != null)
            viewCart.items.remove(itemId);
    }

    public void update(String cartId, String itemId, Integer quantity) {
        // TODO: update item quantity in cart
    }

    public String getItems(String cartId) {
        JsonObject json = new JsonObject();
        ViewCart viewCart = viewCarts.get(cartId);
        if (viewCart == null) {
            json.put("items", new JsonArray());
        } else {
            int size = viewCart.items.size();
            ArrayList<Item> items = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Item item = Item.dao.where().itemid.eq(viewCart.items.get(i)).findOne();
                items.add(item);
            }
            json.put("items", new JsonArray(items));
        }
        return json.encode();
    }
}
