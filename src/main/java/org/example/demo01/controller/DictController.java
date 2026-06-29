package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.entity.Dict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dict")
@Tag(name = "数据字典", description = "数据字典管理相关接口")
public class DictController {

    @Autowired
    private org.example.demo01.mapper.DictMapper dictMapper;

    @GetMapping
    @Operation(summary = "获取字典列表", description = "获取所有字典数据")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<Dict> dicts = dictMapper.selectList(null);
        Map<String, Object> result = new HashMap<>();
        result.put("content", dicts);
        result.put("totalElements", dicts.size());
        result.put("totalPages", 1);
        result.put("currentPage", page);
        result.put("pageSize", size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取字典详情", description = "根据ID获取字典详情")
    public Result<Dict> get(@PathVariable Long id) {
        Dict dict = dictMapper.selectById(id);
        if (dict == null) {
            return Result.errorWithMsg("Dict not found", 404);
        }
        return Result.success(dict);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "按类型获取字典", description = "根据字典类型获取字典列表")
    public Result<List<Dict>> getByType(@PathVariable String type) {
        List<Dict> dicts = dictMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Dict>()
                .eq("type", type)
                .eq("status", 1)
                .orderByAsc("sort")
        );
        return Result.success(dicts);
    }

    @PostMapping
    @Operation(summary = "创建字典", description = "创建新字典")
    public Result<Dict> create(@RequestBody Dict dict) {
        dict.setStatus(1);
        dict.setCreatedAt(LocalDateTime.now());
        dictMapper.insert(dict);
        return Result.success(dict);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新字典", description = "更新字典信息")
    public Result<Dict> update(@PathVariable Long id, @RequestBody Dict dict) {
        Dict existing = dictMapper.selectById(id);
        if (existing == null) {
            return Result.errorWithMsg("Dict not found", 404);
        }
        dict.setId(id);
        dictMapper.updateById(dict);
        return Result.success(dict);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典", description = "删除字典")
    public Result<Void> delete(@PathVariable Long id) {
        Dict dict = dictMapper.selectById(id);
        if (dict == null) {
            return Result.errorWithMsg("Dict not found", 404);
        }
        dictMapper.deleteById(id);
        return Result.success();
    }
}