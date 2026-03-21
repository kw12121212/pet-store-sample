package com.lealone.examples.petstore.service;

import com.lealone.examples.petstore.model.Account;
import com.lealone.examples.petstore.model.User;
import com.lealone.plugins.orm.json.JsonObject;

public class UserService {

    public User login(String userId, String password) {
        User user = User.dao.where().userId.eq(userId).and().password.eq(password).findOne();
        if (user == null)
            throw new RuntimeException("用户名不存在或密码错误");
        return user;
    }

    public String logout(String userId) {
        return "ok";
    }

    public void register(String userId, String password, String password2) {
        new User().userId.set(userId).password.set(password).insert();
    }

    public void update(Account account) {
        Account old = Account.dao.where().userId.eq(account.userId.get()).findOne();
        if (old == null)
            account.insert();
        else
            account.update();
    }

    public String getUser(String userId) {
        User user = User.dao.where().userId.eq(userId).findOne();
        Account account = Account.dao.where().userId.eq(userId).findOne();
        if (account == null)
            account = new Account();
        JsonObject json = new JsonObject();
        json.put("user", user);
        json.put("account", account);
        return json.encode();
    }
}
