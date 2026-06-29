package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;
import org.example.demo01.entity.Position;
import org.example.demo01.mapper.CandidateMapper;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private PositionMapper positionMapper;

    public static final Integer STATUS_PENDING = 1;
    public static final Integer STATUS_SCREENING = 2;
    public static final Integer STATUS_INTERVIEWING = 3;
    public static final Integer STATUS_OFFERED = 4;
    public static final Integer STATUS_HIRED = 5;
    public static final Integer STATUS_REJECTED = 6;

    @Override
    @Transactional(readOnly = true)
    public IPage<Candidate> page(Integer page, Integer size, String candidateName, String status, Long positionId) {
        Page<Candidate> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
        if (candidateName != null && !candidateName.isEmpty()) {
            wrapper.like(Candidate::getName, candidateName);
        }
        if (status != null && !status.isEmpty()) {
            Integer statusInt = parseStatusFilter(status);
            if (statusInt != null) {
                wrapper.eq(Candidate::getStatus, statusInt);
            }
        }
        if (positionId != null) {
            wrapper.eq(Candidate::getPositionId, positionId);
        }
        wrapper.orderByDesc(Candidate::getCreatedAt);
        return candidateMapper.selectPage(pageParam, wrapper);
    }

    private Integer parseStatusFilter(String status) {
        if (status == null) {
            return null;
        }
        try {
            return Integer.parseInt(status);
        } catch (NumberFormatException e) {
            switch (status) {
                case "pending": return 1;
                case "screening": return 2;
                case "interviewing": return 3;
                case "offered": return 4;
                case "hired": return 5;
                case "rejected": return 6;
                default: return null;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Candidate getById(Long id) {
        Candidate candidate = candidateMapper.selectById(id);
        if (candidate != null && candidate.getPositionId() != null) {
            Position position = positionMapper.selectById(candidate.getPositionId());
            if (position != null) {
                candidate.setPositionName(position.getName());
            }
        }
        return candidate;
    }

    @Override
    @Transactional
    public boolean create(Candidate candidate) {
        candidate.setCreatedAt(LocalDateTime.now());
        if (candidate.getStatus() == null) {
            candidate.setStatus(STATUS_PENDING);
        }
        if (candidate.getInterviewCount() == null) {
            candidate.setInterviewCount(0);
        }
        return candidateMapper.insert(candidate) > 0;
    }

    @Override
    @Transactional
    public boolean update(Candidate candidate) {
        LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Candidate::getId, candidate.getId());
        return candidateMapper.update(candidate, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return candidateMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public String transitionStatus(Long id, String action) {
        Candidate candidate = candidateMapper.selectById(id);
        if (candidate == null) {
            return null;
        }

        Integer currentStatus = candidate.getStatus();
        Integer newStatus = currentStatus;

        switch (action) {
            case "pass":
                if (STATUS_PENDING.equals(currentStatus)) {
                    newStatus = STATUS_SCREENING;
                } else if (STATUS_SCREENING.equals(currentStatus)) {
                    newStatus = STATUS_INTERVIEWING;
                    candidate.setInterviewCount(candidate.getInterviewCount() + 1);
                }
                break;
            case "reject":
                newStatus = STATUS_REJECTED;
                break;
            case "offer":
                if (STATUS_INTERVIEWING.equals(currentStatus)) {
                    newStatus = STATUS_OFFERED;
                    candidate.setOfferStatus("pending");
                }
                break;
            case "confirm":
                if (STATUS_OFFERED.equals(currentStatus)) {
                    newStatus = STATUS_HIRED;
                    candidate.setOfferStatus("accepted");
                }
                break;
            default:
                break;
        }

        if (!newStatus.equals(currentStatus)) {
            candidate.setStatus(newStatus);
            LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Candidate::getId, id);
            candidateMapper.update(candidate, wrapper);
        }

        return statusIntToString(newStatus);
    }

    private String statusIntToString(Integer status) {
        switch (status) {
            case 1: return "pending";
            case 2: return "screening";
            case 3: return "interviewing";
            case 4: return "offered";
            case 5: return "hired";
            case 6: return "rejected";
            default: return "unknown";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PageResult<Candidate>> getCandidatePage(PageRequest pageRequest) {
        IPage<Candidate> pageResult = page(
            pageRequest.getPage(),
            pageRequest.getSize(),
            pageRequest.getKeyword(),
            pageRequest.getStatus() != null ? pageRequest.getStatus().toString() : null,
            null
        );
        List<Candidate> records = pageResult.getRecords();
        for (Candidate candidate : records) {
            if (candidate.getPositionId() != null) {
                Position position = positionMapper.selectById(candidate.getPositionId());
                if (position != null) {
                    candidate.setPositionName(position.getName());
                }
            }
        }
        PageResult<Candidate> result = new PageResult<>();
        result.setContent(records);
        result.setTotalElements(pageResult.getTotal());
        result.setTotalPages((int) pageResult.getPages());
        result.setCurrentPage(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Candidate> addCandidate(Candidate candidate) {
        candidate.setCreatedAt(LocalDateTime.now());
        if (candidate.getStatus() == null) {
            candidate.setStatus(STATUS_PENDING);
        }
        candidateMapper.insert(candidate);
        return Result.success(candidate);
    }

    @Override
    @Transactional
    public Result<Candidate> updateCandidate(Candidate candidate) {
        LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Candidate::getId, candidate.getId());
        candidateMapper.update(candidate, wrapper);
        return Result.success(candidate);
    }

    @Override
    @Transactional
    public Result<Void> updateStatus(Long id, String status) {
        Candidate candidate = candidateMapper.selectById(id);
        if (candidate != null) {
            Integer statusInt = parseStatus(status);
            if (statusInt != null) {
                candidate.setStatus(statusInt);
                LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Candidate::getId, id);
                candidateMapper.update(candidate, wrapper);
            }
        }
        return Result.success();
    }

    private Integer parseStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return Integer.parseInt(status);
        } catch (NumberFormatException e) {
            return parseStatusFilter(status);
        }
    }

    @Override
    @Transactional
    public Result<Void> addFollowRecord(Long id, String record) {
        Candidate candidate = candidateMapper.selectById(id);
        if (candidate != null) {
            String currentRecords = candidate.getFollowRecords();
            String newRecords = currentRecords == null ? record : currentRecords + "\n" + record;
            candidate.setFollowRecords(newRecords);
            LambdaQueryWrapper<Candidate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Candidate::getId, id);
            candidateMapper.update(candidate, wrapper);
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> deleteCandidate(Long id) {
        candidateMapper.deleteById(id);
        return Result.success();
    }
}
