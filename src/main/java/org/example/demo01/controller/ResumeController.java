package org.example.demo01.controller;

import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Resume;
import org.example.demo01.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 简历控制器
 * 处理简历信息的增删改查和文件上传
 */
@RestController
@RequestMapping("/api/resumes")
@Tag(name = "简历管理", description = "简历信息相关接口")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 分页查询简历列表
     * @param pageRequest 分页请求参数
     * @return 简历分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询简历列表", description = "支持分页和条件筛选的简历列表查询")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "keyword", description = "关键词搜索"),
        @Parameter(name = "candidateId", description = "候选人ID")
    })
    public Result<PageResult<Resume>> listResumes(
            @ModelAttribute PageRequest pageRequest) {
        return resumeService.getResumePage(pageRequest);
    }

    /**
     * 添加简历
     * @param resume 简历信息
     * @return 添加结果
     */
    @PostMapping
    @Operation(summary = "添加简历", description = "创建新的简历信息")
    public Result<Resume> addResume(@RequestBody Resume resume) {
        return resumeService.addResume(resume);
    }

    /**
     * 更新简历
     * @param id 简历ID
     * @param resume 简历信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新简历", description = "根据ID更新简历信息")
    @Parameter(name = "id", description = "简历ID", required = true)
    public Result<Resume> updateResume(
            @PathVariable Long id,
            @RequestBody Resume resume) {
        resume.setId(id);
        return resumeService.updateResume(resume);
    }

    /**
     * 删除简历
     * @param id 简历ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除简历", description = "根据ID删除简历")
    @Parameter(name = "id", description = "简历ID", required = true)
    public Result<Void> deleteResume(@PathVariable Long id) {
        return resumeService.deleteResume(id);
    }

    /**
     * 上传简历文件
     * @param file 文件
     * @param candidateId 候选人ID
     * @return 上传结果
     */
    @PostMapping("/upload")
    @Operation(summary = "上传简历文件", description = "上传简历文档，支持PDF、Word等格式")
    @Parameters({
        @Parameter(name = "file", description = "简历文件", required = true),
        @Parameter(name = "candidateId", description = "候选人ID")
    })
    public Result<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long candidateId) {
        return resumeService.uploadResume(file, candidateId);
    }

    @GetMapping("/unconverted")
    @Operation(summary = "获取未转候选人的简历列表", description = "查询尚未转换为候选人的简历，支持姓名/手机号搜索")
    @Parameter(name = "keyword", description = "搜索关键词（姓名或手机号）")
    public Result<List<Resume>> getUnconvertedResumes(
            @RequestParam(required = false) String keyword) {
        return resumeService.getUnconvertedResumes(keyword);
    }
}
