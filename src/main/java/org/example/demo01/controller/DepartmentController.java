package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.entity.Department;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "部门管理", description = "部门管理相关接口")
public class DepartmentController {

    @Autowired
    private org.example.demo01.mapper.DepartmentMapper departmentMapper;

    @GetMapping
    @Operation(summary = "获取部门列表", description = "获取所有部门")
    public Result<List<Department>> list() {
        List<Department> departments = departmentMapper.selectList(null);
        return Result.success(departments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取部门详情")
    public Result<Department> get(@PathVariable Long id) {
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            return Result.errorWithMsg("Department not found", 404);
        }
        return Result.success(department);
    }

    @PostMapping
    @Operation(summary = "创建部门", description = "创建新部门")
    public Result<Department> create(@RequestBody Department department) {
        department.setStatus(1);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        departmentMapper.insert(department);
        return Result.success(department);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新部门", description = "更新部门信息")
    public Result<Department> update(@PathVariable Long id, @RequestBody Department department) {
        Department existing = departmentMapper.selectById(id);
        if (existing == null) {
            return Result.errorWithMsg("Department not found", 404);
        }
        department.setId(id);
        department.setUpdatedAt(LocalDateTime.now());
        departmentMapper.updateById(department);
        return Result.success(department);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "删除部门")
    public Result<Void> delete(@PathVariable Long id) {
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            return Result.errorWithMsg("Department not found", 404);
        }
        departmentMapper.deleteById(id);
        return Result.success();
    }
}