package com.example.codejudge.controller;

import com.example.codejudge.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam(required = false,defaultValue = "USER") String role) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.register(username, password, role);
            result.put("success", true);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password) {
        boolean success = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);

        if (success) {
            String role = userService.getRoleByUsername(username); // ✅ 获取角色
            result.put("role", role);
            result.put("message", "登录成功");
        } else {
            result.put("message", "用户名或密码错误");
        }

        return result;
    }
    @PostMapping("/rename")
    public Map<String, Object> rename(@RequestParam String oldUsername,
                                      @RequestParam String newUsername) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.renameUsername(oldUsername, newUsername);
            result.put("success", true);
            result.put("message", "用户名修改成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/reset")
    public Map<String, Object> resetPassword(@RequestParam String username,
                                             @RequestParam String oldPassword,
                                             @RequestParam String newPassword) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.resetPassword(username, oldPassword, newPassword);
            result.put("success", true);
            result.put("message", "密码修改成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
