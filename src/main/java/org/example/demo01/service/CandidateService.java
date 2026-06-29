package org.example.demo01.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;

public interface CandidateService {

    IPage<Candidate> page(Integer page, Integer size, String candidateName, String status, Long positionId);

    Candidate getById(Long id);

    boolean create(Candidate candidate);

    boolean update(Candidate candidate);

    boolean delete(Long id);

    String transitionStatus(Long id, String action);

    Result<PageResult<Candidate>> getCandidatePage(PageRequest pageRequest);

    Result<Candidate> addCandidate(Candidate candidate);

    Result<Candidate> updateCandidate(Candidate candidate);

    Result<Void> updateStatus(Long id, String status);

    Result<Void> deleteCandidate(Long id);

    Result<Void> addFollowRecord(Long id, String record);
}
