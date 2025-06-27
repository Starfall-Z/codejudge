package com.example.codejudge.dao;

import com.example.codejudge.model.User;

public interface UserDao {
    void save(User user);
    User findByUsername(String username);
    void renameUsername(String oldUsername, String newUsername);
    void updatePassword(String username, String hashedPassword);
}

