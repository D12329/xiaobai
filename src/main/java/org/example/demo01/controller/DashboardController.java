package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数据看板控制器
 * 处理招聘数据统计相关接口
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "数据看板", description = "招聘数据统计相关接口")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取统计数据
     * @return 统计数据
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取统计数据", description = "获取招聘相关的统计数据")
    public Result<Map<String, Object>> getStatistics() {
        return dashboardService.getStatistics();
    }

    /**
     * 获取岗位需求统计
     * @return 岗位需求统计数据
     */
    @GetMapping("/position-demand")
    @Operation(summary = "获取岗位需求统计", description = "获取各岗位的需求统计数据")
    public Result<Map<String, Object>> getPositionDemand() {
        return dashboardService.getPositionDemand();
    }

    /**
     * 获取渠道分布统计
     * @return 渠道分布统计数据
     */
    @GetMapping("/channel-distribution")
    @Operation(summary = "获取渠道分布统计", description = "获取简历来源渠道的分布统计")
    public Result<Map<String, Object>> getChannelDistribution() {
        return dashboardService.getChannelDistribution();
    }

    /**
     * 获取招聘周期统计
     * @return 招聘周期统计数据
     */
    @GetMapping("/recruitment-cycle")
    @Operation(summary = "获取招聘周期统计", description = "获取招聘周期的统计数据")
    public Result<Map<String, Object>> getRecruitmentCycle() {
        return dashboardService.getRecruitmentCycle();
    }

    @GetMapping("/pipeline-stats")
    @Operation(summary = "获取招聘管道统计", description = "获取各阶段候选人数量统计")
    public Result<Map<String, Object>> getPipelineStats() {
        return dashboardService.getPipelineStats();
    }

    @GetMapping("/alerts")
    @Operation(summary = "获取超时预警", description = "获取超时预警列表")
    public Result<Map<String, Object>> getAlerts() {
        return dashboardService.getAlerts();
    }

    @GetMapping("/cycle-stats")
    @Operation(summary = "获取周期统计", description = "获取各环节耗时统计")
    public Result<Map<String, Object>> getCycleStats() {
        return dashboardService.getCycleStats();
    }

    @GetMapping("/position-stats")
    @Operation(summary = "获取职位统计", description = "获取职位招聘进度统计")
    public Result<Map<String, Object>> getPositionStats() {
        return dashboardService.getPositionStats();
    }

    @GetMapping("/tracking")
    @Operation(summary = "获取追踪数据", description = "获取招聘流程追踪数据")
    public Result<Map<String, Object>> getTrackingData() {
        return dashboardService.getTrackingData();
    }
}
