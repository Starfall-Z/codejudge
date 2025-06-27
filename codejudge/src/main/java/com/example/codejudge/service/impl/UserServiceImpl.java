package com.example.codejudge.service.impl;

import com.example.codejudge.dao.UserDao;
import com.example.codejudge.model.User;
import com.example.codejudge.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void register(String username, String password, String role) {
        if (userDao.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(role == null || role.isEmpty() ? "USER" : role);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userDao.save(user);
    }

    @Override
    public boolean login(String username, String password) {
        User user = userDao.findByUsername(username);
        return user != null && encoder.matches(password, user.getPassword());
    }

    @Override
    public String getRoleByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user != null ? user.getRole() : "USER";  // 默认 USER
    }

    @Override
    public void renameUsername(String oldUsername, String newUsername) {
        if (userDao.findByUsername(newUsername) != null) {
            throw new RuntimeException("新用户名已被占用");
        }
        userDao.renameUsername(oldUsername, newUsername);
    }

    @Override
    public void resetPassword(String username, String oldPassword, String newPassword) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        String hashed = encoder.encode(newPassword);
        userDao.updatePassword(username, hashed);
    }

}