package com.example.codejudge.service;

public interface UserService {
    void register(String username, String password, String role);
    boolean login(String username, String password);
    String getRoleByUsername(String username);
    void renameUsername(String oldUsername, String newUsername);
    void resetPassword(String username, String oldPassword, String newPassword);
}
