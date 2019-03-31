package com.itheima.login.service.impl;

import com.itheima.login.dao.UserDao;
import com.itheima.login.dao.impl.UserDaoImpl;
import com.itheima.login.dao.impl.UserDaoImpl2;
import com.itheima.login.domain.User;
import com.itheima.login.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User login(User user) {
        UserDao userDao = new UserDaoImpl2();
        return userDao.findUserByUsernameAndPassword(user);

    }
}
