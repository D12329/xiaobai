package org.example.demo01.controller;

import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;
import org.example.demo01.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 候选人控制器
 * 处理候选人信息的增删改查、状态更新和跟进记录
 */
@RestController
@RequestMapping("/api/candidates")
@Tag(name = "候选人管理", description = "候选人信息相关接口")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    /**
     * 分页查询候选人列表
     * @param pageRequest 分页请求参数
     * @return 候选人分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询候选人列表", description = "支持分页和条件筛选的候选人列表查询")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "keyword", description = "关键词搜索"),
        @Parameter(name = "status", description = "候选人状态"),
        @Parameter(name = "positionId", description = "岗位ID")
    })
    public Result<PageResult<Candidate>> listCandidates(
            @ModelAttribute PageRequest pageRequest) {
        return candidateService.getCandidatePage(pageRequest);
    }

    /**
     * 添加候选人
     * @param candidate 候选人信息
     * @return 添加结果
     */
    @PostMapping
    @Operation(summary = "添加候选人", description = "创建新的候选人信息")
    public Result<Candidate> addCandidate(@RequestBody Candidate candidate) {
        return candidateService.addCandidate(candidate);
    }

    /**
     * 更新候选人
     * @param id 候选人ID
     * @param candidate 候选人信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新候选人", description = "根据ID更新候选人信息")
    @Parameter(name = "id", description = "候选人ID", required = true)
    public Result<Candidate> updateCandidate(
            @PathVariable Long id,
            @RequestBody Candidate candidate) {
        candidate.setId(id);
        return candidateService.updateCandidate(candidate);
    }

    /**
     * 更新候选人状态
     * @param id 候选人ID
     * @param status 新状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新候选人状态", description = "根据ID更新候选人状态")
    @Parameters({
        @Parameter(name = "id", description = "候选人ID", required = true),
        @Parameter(name = "status", description = "新状态", required = true)
    })
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return candidateService.updateStatus(id, status);
    }

    /**
     * 删除候选人
     * @param id 候选人ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除候选人", description = "根据ID删除候选人")
    @Parameter(name = "id", description = "候选人ID", required = true)
    public Result<Void> deleteCandidate(@PathVariable Long id) {
        return candidateService.deleteCandidate(id);
    }

    /**
     * 添加跟进记录
     * @param id 候选人ID
     * @param followData 跟进数据
     * @return 添加结果
     */
    @PostMapping("/{id}/follow")
    @Operation(summary = "添加跟进记录", description = "为候选人添加跟进记录")
    @Parameters({
        @Parameter(name = "id", description = "候选人ID", required = true)
    })
    public Result<Void> addFollow(
            @PathVariable Long id,
            @RequestBody Map<String, Object> followData) {
        String record = (String) followData.get("record");
        return candidateService.addFollowRecord(id, record);
    }
}
