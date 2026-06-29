package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Resume;
import org.example.demo01.mapper.ResumeMapper;
import org.example.demo01.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    private static final String UPLOAD_DIR = "upload/resumes";

    @Override
    @Transactional(readOnly = true)
    public IPage<Resume> page(Integer page, Integer size, String candidateName) {
        Page<Resume> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        if (candidateName != null && !candidateName.isEmpty()) {
            wrapper.like(Resume::getName, candidateName);
        }
        wrapper.orderByDesc(Resume::getCreatedAt);
        return resumeMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Resume getById(Long id) {
        return resumeMapper.selectById(id);
    }

    @Override
    @Transactional
    public Map<String, Object> upload(Object file, Resume resume) {
        Map<String, Object> result = new HashMap<>();
        if (file instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) file;
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uuidFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

            File uploadPath = new File(UPLOAD_DIR);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File destFile = new File(uploadPath, uuidFileName);
            try {
                multipartFile.transferTo(destFile);
                resume.setFilePath(UPLOAD_DIR + "/" + uuidFileName);
            } catch (IOException e) {
                result.put("success", false);
                result.put("message", "File upload failed");
                return result;
            }
        }

        resume.setCreatedAt(LocalDateTime.now());
        resumeMapper.insert(resume);

        result.put("success", true);
        result.put("resume", resume);
        return result;
    }

    @Override
    @Transactional
    public boolean create(Resume resume) {
        resume.setCreatedAt(LocalDateTime.now());
        return resumeMapper.insert(resume) > 0;
    }

    @Override
    @Transactional
    public boolean update(Resume resume) {
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getId, resume.getId());
        return resumeMapper.update(resume, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null && resume.getFilePath() != null) {
            File file = new File(resume.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        }
        return resumeMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PageResult<Resume>> getResumePage(PageRequest pageRequest) {
        IPage<Resume> pageResult = page(
                pageRequest.getPage(),
                pageRequest.getSize(),
                pageRequest.getKeyword()
        );
        PageResult<Resume> result = new PageResult<>();
        result.setContent(pageResult.getRecords());
        result.setTotalElements(pageResult.getTotal());
        result.setTotalPages((int) pageResult.getPages());
        result.setCurrentPage(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Resume> addResume(Resume resume) {
        resume.setCreatedAt(LocalDateTime.now());
        resumeMapper.insert(resume);
        return Result.success(resume);
    }

    @Override
    @Transactional
    public Result<Resume> updateResume(Resume resume) {
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getId, resume.getId());
        resumeMapper.update(resume, wrapper);
        return Result.success(resume);
    }

    @Override
    @Transactional
    public Result<Void> deleteResume(Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null && resume.getFilePath() != null) {
            File file = new File(resume.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        }
        resumeMapper.deleteById(id);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<String> uploadResume(MultipartFile file, Long candidateId) {
        if (file == null || file.isEmpty()) {
            return Result.errorWithMsg("File is empty", 400);
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uuidFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        File uploadPath = new File(UPLOAD_DIR);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destFile = new File(uploadPath, uuidFileName);
        try {
            file.transferTo(destFile);

            Resume resume = new Resume();
            resume.setFilePath(UPLOAD_DIR + "/" + uuidFileName);
            resume.setCreatedAt(LocalDateTime.now());
            if (candidateId != null) {
                resume.setId(candidateId);
            }
            resumeMapper.insert(resume);

            return Result.success("File uploaded successfully");
        } catch (IOException e) {
            return Result.errorWithMsg("File upload failed: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<Resume>> getUnconvertedResumes(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        List<Resume> resumes = resumeMapper.selectUnconvertedResumes(keyword);
        return Result.success(resumes);
    }
}
