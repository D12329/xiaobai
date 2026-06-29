package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.entity.User;
import org.example.demo01.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private org.example.demo01.mapper.UserMapper userMapper;

    @Autowired
    private org.example.demo01.mapper.RoleMapper roleMapper;

    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        
        List<User> users = userMapper.selectList(null);
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", users);
        result.put("totalElements", users.size());
        result.put("totalPages", 1);
        result.put("currentPage", page);
        result.put("pageSize", size);
        
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情")
    public Result<User> get(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        return Result.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<User> create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return Result.success(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existing.getPassword());
        }
        
        user.setId(id);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        userMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping("/roles")
    @Operation(summary = "获取角色列表", description = "获取所有角色")
    public Result<List<Map<String, Object>>> getRoles() {
        List<Map<String, Object>> roles = roleMapper.selectMaps(null);
        return Result.success(roles);
    }
}