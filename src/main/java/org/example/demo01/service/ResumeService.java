package org.example.demo01.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResumeService {

    IPage<Resume> page(Integer page, Integer size, String candidateName);

    Resume getById(Long id);

    Map<String, Object> upload(Object file, Resume resume);

    boolean create(Resume resume);

    boolean update(Resume resume);

    boolean delete(Long id);

    Result<PageResult<Resume>> getResumePage(PageRequest pageRequest);

    Result<Resume> addResume(Resume resume);

    Result<Resume> updateResume(Resume resume);

    Result<Void> deleteResume(Long id);

    Result<String> uploadResume(MultipartFile file, Long candidateId);

    Result<List<Resume>> getUnconvertedResumes(String keyword);
}
