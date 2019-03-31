package com.itheima.login.dao.impl;

import com.itheima.login.dao.UserDao;
import com.itheima.login.domain.User;
import com.itheima.login.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsernameAndPassword(User user) {
        try {

            String sql = "select * from user where username=? and password=?";
            User existUser = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(), user.getPassword());
            return existUser;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
