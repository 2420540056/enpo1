package com.itheima.login.dao;

import com.itheima.login.domain.User;

public interface UserDao {
    User findUserByUsernameAndPassword(User user);
}
