package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.entity.User;
import org.example.demo01.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、登出和获取用户信息
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param loginData 登录数据（username, password）
     * @return 登录结果（包含token）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，返回JWT token")
    @Parameters({
        @Parameter(name = "username", description = "用户名", required = true),
        @Parameter(name = "password", description = "密码", required = true)
    })
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        return userService.login(username, password);
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public Result<Void> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return Result.success();
        } else {
            return Result.errorWithMsg("用户名已存在", 400);
        }
    }

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/userinfo")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getUserInfo() {
        return userService.getUserInfo();
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public Result<Void> logout() {
        return userService.logout();
    }

    /**
     * 修改密码
     * @param changePasswordData 包含 userId, oldPassword, newPassword
     * @return 修改结果
     */
    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "用户修改密码接口")
    @Parameters({
        @Parameter(name = "userId", description = "用户ID", required = true),
        @Parameter(name = "oldPassword", description = "旧密码", required = true),
        @Parameter(name = "newPassword", description = "新密码", required = true)
    })
    public Result<Void> changePassword(@RequestBody Map<String, String> changePasswordData) {
        Long userId = Long.parseLong(changePasswordData.get("userId"));
        String oldPassword = changePasswordData.get("oldPassword");
        String newPassword = changePasswordData.get("newPassword");
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 重置密码（无需旧密码，仅用于初始化或管理员操作）
     * @param resetPasswordData 包含 username 和 newPassword
     * @return 重置结果
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "重置用户密码（无需旧密码）")
    @Parameters({
        @Parameter(name = "username", description = "用户名", required = true),
        @Parameter(name = "newPassword", description = "新密码", required = true)
    })
    public Result<Void> resetPassword(@RequestBody Map<String, String> resetPasswordData) {
        String username = resetPasswordData.get("username");
        String newPassword = resetPasswordData.get("newPassword");
        return userService.resetPassword(username, newPassword);
    }
}
