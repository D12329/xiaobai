package org.example.demo01.controller;

import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Interview;
import org.example.demo01.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 面试控制器
 * 处理面试安排、状态更新和评价提交
 */
@RestController
@RequestMapping("/api/interviews")
@Tag(name = "面试管理", description = "面试相关接口")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    /**
     * 分页查询面试列表
     * @param pageRequest 分页请求参数
     * @return 面试分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询面试列表", description = "支持分页和条件筛选的面试列表查询")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "status", description = "面试状态"),
        @Parameter(name = "candidateId", description = "候选人ID"),
        @Parameter(name = "positionId", description = "岗位ID")
    })
    public Result<PageResult<Interview>> listInterviews(
            @ModelAttribute PageRequest pageRequest) {
        return interviewService.getInterviewPage(pageRequest);
    }

    /**
     * 添加面试
     * @param interview 面试信息
     * @return 添加结果
     */
    @PostMapping
    @Operation(summary = "添加面试", description = "创建新的面试安排")
    public Result<Interview> addInterview(@RequestBody Interview interview) {
        return interviewService.addInterview(interview);
    }

    /**
     * 更新面试
     * @param id 面试ID
     * @param interview 面试信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新面试", description = "根据ID更新面试信息")
    @Parameter(name = "id", description = "面试ID", required = true)
    public Result<Interview> updateInterview(
            @PathVariable Long id,
            @RequestBody Interview interview) {
        interview.setId(id);
        return interviewService.updateInterview(interview);
    }

    /**
     * 更新面试状态
     * @param id 面试ID
     * @param status 新状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新面试状态", description = "根据ID更新面试状态")
    @Parameters({
        @Parameter(name = "id", description = "面试ID", required = true),
        @Parameter(name = "status", description = "新状态", required = true)
    })
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return interviewService.updateStatus(id, status);
    }

    /**
     * 提交面试评价
     * @param id 面试ID
     * @param evaluationData 评价数据
     * @return 评价结果
     */
    @PutMapping("/{id}/evaluation")
    @Operation(summary = "提交面试评价", description = "提交面试评价和评分")
    @Parameter(name = "id", description = "面试ID", required = true)
    public Result<Void> submitEvaluation(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> evaluationData) {
        String evaluation = (String) evaluationData.get("evaluation");
        Integer score = (Integer) evaluationData.get("score");
        return interviewService.submitEvaluation(id, evaluation, score);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除面试", description = "根据ID删除面试安排")
    @Parameter(name = "id", description = "面试ID", required = true)
    public Result<Void> deleteInterview(@PathVariable Long id) {
        if (interviewService.delete(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
