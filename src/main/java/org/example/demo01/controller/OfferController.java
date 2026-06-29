package org.example.demo01.controller;

import org.example.demo01.common.PageRequest;
import org.example.demo01.common.PageResult;
import org.example.demo01.common.Result;
import org.example.demo01.entity.Offer;
import org.example.demo01.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Offer控制器
 * 处理Offer管理相关接口
 */
@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offer管理", description = "Offer相关接口")
public class OfferController {

    @Autowired
    private OfferService offerService;

    /**
     * 分页查询Offer列表
     * @param pageRequest 分页请求参数
     * @return Offer分页列表
     */
    @GetMapping
    @Operation(summary = "分页查询Offer列表", description = "支持分页和条件筛选的Offer列表查询")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "status", description = "Offer状态"),
        @Parameter(name = "candidateId", description = "候选人ID"),
        @Parameter(name = "positionId", description = "岗位ID")
    })
    public Result<PageResult<Offer>> listOffers(
            @ModelAttribute PageRequest pageRequest) {
        return offerService.getOfferPage(pageRequest);
    }

    /**
     * 添加Offer
     * @param offer Offer信息
     * @return 添加结果
     */
    @PostMapping
    @Operation(summary = "添加Offer", description = "创建新的Offer")
    public Result<Offer> addOffer(@RequestBody Offer offer) {
        return offerService.addOffer(offer);
    }

    /**
     * 更新Offer
     * @param id OfferID
     * @param offer Offer信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新Offer", description = "根据ID更新Offer信息")
    @Parameter(name = "id", description = "OfferID", required = true)
    public Result<Offer> updateOffer(
            @PathVariable Long id,
            @RequestBody Offer offer) {
        offer.setId(id);
        return offerService.updateOffer(offer);
    }

    /**
     * 删除Offer
     * @param id OfferID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除Offer", description = "根据ID删除Offer")
    @Parameter(name = "id", description = "OfferID", required = true)
    public Result<Void> deleteOffer(@PathVariable Long id) {
        return offerService.deleteOffer(id);
    }

    /**
     * 更新Offer状态
     * @param id OfferID
     * @param status 新状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新Offer状态", description = "根据ID更新Offer状态")
    @Parameters({
        @Parameter(name = "id", description = "OfferID", required = true),
        @Parameter(name = "status", description = "新状态", required = true)
    })
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return offerService.updateStatus(id, status);
    }
}
