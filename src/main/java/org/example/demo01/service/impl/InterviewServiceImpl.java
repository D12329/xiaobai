package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;
import org.example.demo01.entity.Interview;
import org.example.demo01.entity.Position;
import org.example.demo01.entity.User;
import org.example.demo01.mapper.CandidateMapper;
import org.example.demo01.mapper.InterviewMapper;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.mapper.UserMapper;
import org.example.demo01.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private UserMapper userMapper;

    public static final String STATUS_SCHEDULED = "scheduled";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PASSED = "passed";
    public static final String STATUS_FAILED = "failed";

    @Override
    @Transactional(readOnly = true)
    public IPage<Interview> page(Integer page, Integer size, Long candidateId, String status, String keyword) {
        Page<Interview> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        if (candidateId != null) {
            wrapper.eq(Interview::getCandidateId, candidateId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Interview::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            List<Candidate> candidates = candidateMapper.selectList(new LambdaQueryWrapper<Candidate>()
                    .like(Candidate::getName, keyword));
            if (!candidates.isEmpty()) {
                List<Long> candidateIds = candidates.stream().map(Candidate::getId).toList();
                wrapper.in(Interview::getCandidateId, candidateIds);
            } else {
                wrapper.eq(Interview::getId, -1);
            }
        }
        wrapper.orderByDesc(Interview::getCreatedAt);
        return interviewMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Interview getById(Long id) {
        return interviewMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean create(Interview interview) {
        interview.setCreatedAt(LocalDateTime.now());
        if (interview.getStatus() == null) {
            interview.setStatus(STATUS_SCHEDULED);
        }
        return interviewMapper.insert(interview) > 0;
    }

    @Override
    @Transactional
    public boolean update(Interview interview) {
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getId, interview.getId());
        return interviewMapper.update(interview, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return interviewMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, String status) {
        Interview interview = interviewMapper.selectById(id);
        if (interview == null) {
            return false;
        }
        interview.setStatus(status);
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getId, id);
        return interviewMapper.update(interview, wrapper) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PageResult<Interview>> getInterviewPage(PageRequest pageRequest) {
        IPage<Interview> pageResult = page(
            pageRequest.getPage(),
            pageRequest.getSize(),
            pageRequest.getCandidateId(),
            pageRequest.getStatus() != null ? pageRequest.getStatus().toString() : null,
            pageRequest.getKeyword()
        );
        List<Interview> records = pageResult.getRecords();
        for (Interview interview : records) {
            if (interview.getCandidateId() != null) {
                Candidate candidate = candidateMapper.selectById(interview.getCandidateId());
                if (candidate != null) {
                    interview.setCandidateName(candidate.getName());
                }
            }
            if (interview.getPositionId() != null) {
                Position position = positionMapper.selectById(interview.getPositionId());
                if (position != null) {
                    interview.setPositionName(position.getName());
                }
            }
            if (interview.getInterviewerId() != null && interview.getInterviewerName() == null) {
                User user = userMapper.selectById(interview.getInterviewerId());
                if (user != null) {
                    interview.setInterviewerName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                }
            }
            if (interview.getStartTime() != null) {
                interview.setInterviewTime(interview.getStartTime());
            }
        }
        PageResult<Interview> result = new PageResult<>();
        result.setContent(records);
        result.setTotalElements(pageResult.getTotal());
        result.setTotalPages((int) pageResult.getPages());
        result.setCurrentPage(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Interview> addInterview(Interview interview) {
        interview.setCreatedAt(LocalDateTime.now());
        if (interview.getStatus() == null) {
            interview.setStatus(STATUS_SCHEDULED);
        }
        if (interview.getPositionId() == null && interview.getCandidateId() != null) {
            Candidate candidate = candidateMapper.selectById(interview.getCandidateId());
            if (candidate != null && candidate.getPositionId() != null) {
                interview.setPositionId(candidate.getPositionId());
            }
        }
        interviewMapper.insert(interview);
        return Result.success(interview);
    }

    @Override
    @Transactional
    public Result<Interview> updateInterview(Interview interview) {
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getId, interview.getId());
        interviewMapper.update(interview, wrapper);
        return Result.success(interview);
    }

    @Override
    @Transactional
    public Result<Void> updateStatus(Long id, Integer status) {
        Interview interview = interviewMapper.selectById(id);
        if (interview != null) {
            String statusStr;
            if (status == 1) {
                statusStr = STATUS_COMPLETED;
            } else if (status == 2) {
                statusStr = STATUS_CANCELLED;
            } else if (status == 3) {
                statusStr = STATUS_PASSED;
            } else if (status == 4) {
                statusStr = STATUS_FAILED;
            } else {
                statusStr = STATUS_PENDING;
            }
            interview.setStatus(statusStr);
            LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Interview::getId, id);
            interviewMapper.update(interview, wrapper);
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> submitEvaluation(Long id, String evaluation, Integer score) {
        Interview interview = interviewMapper.selectById(id);
        if (interview != null) {
            interview.setEvaluation(evaluation);
            interview.setScore(score);
            interview.setStatus(STATUS_COMPLETED);
            LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Interview::getId, id);
            interviewMapper.update(interview, wrapper);
        }
        return Result.success();
    }
}
