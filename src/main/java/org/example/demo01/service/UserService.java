package org.example.demo01.service;

import org.example.demo01.common.Result;
import org.example.demo01.entity.User;

import java.util.Map;

public interface UserService {

    Result<Map<String, Object>> login(String username, String password);

    Result<User> getUserInfo();

    Result<Void> logout();

    User findByUsername(String username);

    boolean register(User user);

    String verifyToken(String token);

    Result<Void> changePassword(Long userId, String oldPassword, String newPassword);

    Result<Void> resetPassword(String username, String newPassword);
}
