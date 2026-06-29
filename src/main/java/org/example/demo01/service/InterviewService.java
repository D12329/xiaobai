package org.example.demo01.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Interview;

public interface InterviewService {

    IPage<Interview> page(Integer page, Integer size, Long candidateId, String status, String keyword);

    Interview getById(Long id);

    boolean create(Interview interview);

    boolean update(Interview interview);

    boolean delete(Long id);

    boolean updateStatus(Long id, String status);

    Result<PageResult<Interview>> getInterviewPage(PageRequest pageRequest);

    Result<Interview> addInterview(Interview interview);

    Result<Interview> updateInterview(Interview interview);

    Result<Void> updateStatus(Long id, Integer status);

    Result<Void> submitEvaluation(Long id, String evaluation, Integer score);
}
