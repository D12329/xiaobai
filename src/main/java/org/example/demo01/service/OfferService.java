package org.example.demo01.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Offer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OfferService {

    IPage<Offer> page(Integer page, Integer size, Long candidateId, String status, String keyword);

    Offer getById(Long id);

    boolean create(Long candidateId, Long positionId, BigDecimal salary, LocalDateTime startDate);

    boolean update(Offer offer);

    boolean delete(Long id);

    boolean updateStatus(Long id, String status);

    boolean send(Long id);

    boolean confirm(Long id, Boolean accepted);

    Result<PageResult<Offer>> getOfferPage(PageRequest pageRequest);

    Result<Offer> addOffer(Offer offer);

    Result<Offer> updateOffer(Offer offer);

    Result<Void> updateStatus(Long id, Integer status);

    Result<Void> deleteOffer(Long id);
}
