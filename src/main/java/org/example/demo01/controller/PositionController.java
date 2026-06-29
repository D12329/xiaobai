package org.example.demo01.controller;

import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Position;
import org.example.demo01.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 职位控制器
 * 处理职位信息的增删改查和状态更新
 */
@RestController
@RequestMapping("/api/positions")
@Tag(name = "职位管理", description = "职位信息相关接口")
public class PositionController {

    @Autowired
    private PositionService positionService;

    /**
     * 分页查询职位列表
     * @param pageRequest 分页请求参数
     * @return 职位分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询职位列表", description = "支持分页和条件筛选的职位列表查询")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "keyword", description = "关键词搜索"),
        @Parameter(name = "status", description = "职位状态"),
        @Parameter(name = "departmentId", description = "部门ID")
    })
    public Result<PageResult<Position>> listPositions(
            @ModelAttribute PageRequest pageRequest) {
        return positionService.getPositionPage(pageRequest);
    }

    /**
     * 添加职位
     * @param position 职位信息
     * @return 添加结果
     */
    @PostMapping
    @Operation(summary = "添加职位", description = "创建新的职位信息")
    public Result<Position> addPosition(@RequestBody Position position) {
        return positionService.addPosition(position);
    }

    /**
     * 更新职位
     * @param id 职位ID
     * @param position 职位信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新职位", description = "根据ID更新职位信息")
    @Parameter(name = "id", description = "职位ID", required = true)
    public Result<Position> updatePosition(
            @PathVariable Long id,
            @RequestBody Position position) {
        position.setId(id);
        return positionService.updatePosition(position);
    }

    /**
     * 删除职位
     * @param id 职位ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除职位", description = "根据ID删除职位")
    @Parameter(name = "id", description = "职位ID", required = true)
    public Result<Void> deletePosition(@PathVariable Long id) {
        return positionService.deletePosition(id);
    }

    /**
     * 更新职位状态
     * @param id 职位ID
     * @param status 新状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新职位状态", description = "根据ID更新职位状态")
    @Parameters({
        @Parameter(name = "id", description = "职位ID", required = true),
        @Parameter(name = "status", description = "新状态", required = true)
    })
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return positionService.updateStatus(id, status);
    }
}
