package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {

    @Autowired
    private org.example.demo01.mapper.RoleMapper roleMapper;

    @GetMapping
    @Operation(summary = "获取角色列表", description = "获取所有角色")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<Role> roles = roleMapper.selectList(null);
        Map<String, Object> result = new HashMap<>();
        result.put("content", roles);
        result.put("totalElements", roles.size());
        result.put("totalPages", 1);
        result.put("currentPage", page);
        result.put("pageSize", size);
        return Result.success(result);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有角色", description = "获取所有角色（不分页）")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleMapper.selectList(null);
        return Result.success(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "根据角色ID获取角色详情")
    public Result<Role> get(@PathVariable Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            return Result.errorWithMsg("Role not found", 404);
        }
        return Result.success(role);
    }
}