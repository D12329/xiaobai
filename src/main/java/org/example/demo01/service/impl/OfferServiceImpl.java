package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Candidate;
import org.example.demo01.entity.Offer;
import org.example.demo01.entity.Position;
import org.example.demo01.mapper.CandidateMapper;
import org.example.demo01.mapper.OfferMapper;
import org.example.demo01.mapper.PositionMapper;
import org.example.demo01.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private PositionMapper positionMapper;

    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_SENT = "sent";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_CANCELLED = "cancelled";

    @Override
    @Transactional(readOnly = true)
    public IPage<Offer> page(Integer page, Integer size, Long candidateId, String status, String keyword) {
        Page<Offer> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        if (candidateId != null) {
            wrapper.eq(Offer::getCandidateId, candidateId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Offer::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            List<Candidate> candidates = candidateMapper.selectList(new LambdaQueryWrapper<Candidate>()
                    .like(Candidate::getName, keyword));
            if (!candidates.isEmpty()) {
                List<Long> candidateIds = candidates.stream().map(Candidate::getId).toList();
                wrapper.in(Offer::getCandidateId, candidateIds);
            } else {
                wrapper.eq(Offer::getId, -1);
            }
        }
        wrapper.orderByDesc(Offer::getCreatedAt);
        return offerMapper.selectPage(pageParam, wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Offer getById(Long id) {
        return offerMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean create(Long candidateId, Long positionId, BigDecimal salary, LocalDateTime startDate) {
        Offer offer = new Offer();
        offer.setCandidateId(candidateId);
        offer.setPositionId(positionId);
        offer.setSalary(salary);
        offer.setStartDate(startDate);
        offer.setStatus(STATUS_DRAFT);
        offer.setCreatedAt(LocalDateTime.now());
        return offerMapper.insert(offer) > 0;
    }

    @Override
    @Transactional
    public boolean update(Offer offer) {
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Offer::getId, offer.getId());
        return offerMapper.update(offer, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return offerMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, String status) {
        Offer offer = offerMapper.selectById(id);
        if (offer == null) {
            return false;
        }
        offer.setStatus(status);
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Offer::getId, id);
        return offerMapper.update(offer, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean send(Long id) {
        Offer offer = offerMapper.selectById(id);
        if (offer == null) {
            return false;
        }
        if (!STATUS_DRAFT.equals(offer.getStatus()) && !STATUS_PENDING.equals(offer.getStatus())) {
            return false;
        }
        offer.setStatus(STATUS_SENT);
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Offer::getId, id);
        return offerMapper.update(offer, wrapper) > 0;
    }

    @Override
    @Transactional
    public boolean confirm(Long id, Boolean accepted) {
        Offer offer = offerMapper.selectById(id);
        if (offer == null) {
            return false;
        }
        if (!STATUS_SENT.equals(offer.getStatus())) {
            return false;
        }
        if (accepted) {
            offer.setStatus(STATUS_ACCEPTED);
        } else {
            offer.setStatus(STATUS_REJECTED);
        }
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Offer::getId, id);
        return offerMapper.update(offer, wrapper) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PageResult<Offer>> getOfferPage(PageRequest pageRequest) {
        String statusStr = null;
        if (pageRequest.getStatus() != null) {
            int status = pageRequest.getStatus();
            if (status == 0) {
                statusStr = STATUS_PENDING;
            } else if (status == 1) {
                statusStr = STATUS_ACCEPTED;
            } else if (status == 2) {
                statusStr = STATUS_REJECTED;
            } else if (status == 3) {
                statusStr = STATUS_CANCELLED;
            } else if (status == 4) {
                statusStr = STATUS_SENT;
            } else {
                statusStr = STATUS_DRAFT;
            }
        }
        IPage<Offer> pageResult = page(
                pageRequest.getPage(),
                pageRequest.getSize(),
                pageRequest.getCandidateId(),
                statusStr,
                pageRequest.getKeyword()
        );
        List<Offer> records = pageResult.getRecords();
        for (Offer offer : records) {
            if (offer.getCandidateId() != null) {
                Candidate candidate = candidateMapper.selectById(offer.getCandidateId());
                if (candidate != null) {
                    offer.setCandidateName(candidate.getName());
                }
            }
            if (offer.getPositionId() != null) {
                Position position = positionMapper.selectById(offer.getPositionId());
                if (position != null) {
                    offer.setPositionName(position.getName());
                }
            }
            if (offer.getSalaryType() == null) {
                offer.setSalaryType("月薪");
            }
        }
        PageResult<Offer> result = new PageResult<>();
        result.setContent(records);
        result.setTotalElements(pageResult.getTotal());
        result.setTotalPages((int) pageResult.getPages());
        result.setCurrentPage(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        return Result.success(result);
    }

    @Override
    @Transactional
    public Result<Offer> addOffer(Offer offer) {
        if (offer.getStatus() == null) {
            offer.setStatus(STATUS_DRAFT);
        }
        if (offer.getCreatedAt() == null) {
            offer.setCreatedAt(LocalDateTime.now());
        }
        if (offer.getPositionId() == null && offer.getCandidateId() != null) {
            Candidate candidate = candidateMapper.selectById(offer.getCandidateId());
            if (candidate != null && candidate.getPositionId() != null) {
                offer.setPositionId(candidate.getPositionId());
            }
        }
        offerMapper.insert(offer);
        return Result.success(offer);
    }

    @Override
    @Transactional
    public Result<Offer> updateOffer(Offer offer) {
        LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Offer::getId, offer.getId());
        offerMapper.update(offer, wrapper);
        return Result.success(offer);
    }

    @Override
    @Transactional
    public Result<Void> updateStatus(Long id, Integer status) {
        Offer offer = offerMapper.selectById(id);
        if (offer != null) {
            String statusStr;
            if (status == 1) {
                statusStr = STATUS_ACCEPTED;
            } else if (status == 2) {
                statusStr = STATUS_REJECTED;
            } else if (status == 3) {
                statusStr = STATUS_CANCELLED;
            } else if (status == 4) {
                statusStr = STATUS_SENT;
            } else {
                statusStr = STATUS_PENDING;
            }
            offer.setStatus(statusStr);
            LambdaQueryWrapper<Offer> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Offer::getId, id);
            offerMapper.update(offer, wrapper);
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> deleteOffer(Long id) {
        offerMapper.deleteById(id);
        return Result.success();
    }
}
