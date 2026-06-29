package org.example.demo01.controller;

import org.example.demo01.common.Result;
import org.example.demo01.service.ResumeMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * AI简历匹配控制器
 * 处理简历解析和人岗匹配相关接口
 */
@RestController
@RequestMapping("/api/match")
@Tag(name = "AI简历匹配", description = "简历解析和人岗匹配相关接口")
public class ResumeMatchController {

    @Autowired
    private ResumeMatchService resumeMatchService;

    /**
     * 解析简历
     * @param resumeId 简历ID
     * @return 解析结果
     */
    @PostMapping("/parse/{resumeId}")
    @Operation(summary = "解析简历", description = "使用AI解析简历中的关键信息")
    @Parameter(name = "resumeId", description = "简历ID", required = true)
    public Result<Map<String, Object>> parseResume(@PathVariable Long resumeId) {
        return resumeMatchService.parseResume(resumeId);
    }

    /**
     * 人岗匹配
     * @param positionId 职位ID
     * @param matchData 匹配数据
     * @return 匹配结果列表
     */
    @PostMapping("/position/{positionId}")
    @Operation(summary = "人岗匹配", description = "根据职位要求匹配候选人")
    @Parameter(name = "positionId", description = "职位ID", required = true)
    public Result<List<Map<String, Object>>> matchPosition(
            @PathVariable Long positionId,
            @RequestBody Map<String, Object> matchData) {
        @SuppressWarnings("unchecked")
        List<Number> resumeIdNumbers = (List<Number>) matchData.get("resumeIds");
        List<Long> resumeIds = new ArrayList<>();
        if (resumeIdNumbers != null) {
            for (Number num : resumeIdNumbers) {
                resumeIds.add(num.longValue());
            }
        }
        Integer threshold = (Integer) matchData.get("threshold");
        return resumeMatchService.matchPosition(positionId, resumeIds, threshold);
    }

    /**
     * 获取匹配结果
     * @param candidateId 候选人ID
     * @return 匹配结果
     */
    @GetMapping("/result/{candidateId}")
    @Operation(summary = "获取匹配结果", description = "获取候选人的匹配结果")
    @Parameter(name = "candidateId", description = "候选人ID", required = true)
    public Result<Map<String, Object>> getMatchResult(@PathVariable Long candidateId) {
        return resumeMatchService.getMatchResult(candidateId);
    }
}
