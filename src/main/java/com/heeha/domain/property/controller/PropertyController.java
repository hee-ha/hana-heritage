package com.heeha.domain.property.controller;

import com.heeha.domain.property.dto.PropertyResponse;
import com.heeha.domain.property.service.PropertyService;
import com.heeha.global.config.BaseResponse;
import com.heeha.global.config.BaseResponse.ErrorResult;
import com.heeha.global.config.BaseResponse.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/livingtrust/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Operation(summary = "자산 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "자산 목록 조회 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
            @ApiResponse(responseCode = "11100", description = "잘못된 신탁 아이디값", content = @Content(schema = @Schema(implementation = ErrorResult.class))),
            @ApiResponse(responseCode = "11200", description = "등록된 자산 목록 없음 (가능 : 금전, 금전채권, 유가증권, 부동산)", content = @Content(schema = @Schema(implementation = ErrorResult.class))),
    })
    @GetMapping("/my")
    public SuccessResult<Map<String,List<PropertyResponse>>> getProperty(@RequestParam Long livingtrustId) {
        log.info("자산 목록 조회 시도 : {}", livingtrustId);
        return BaseResponse.success(propertyService.getAllProperties(livingtrustId));
    }

    @Operation(summary = "자산 차트 데이터 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "자산 목록 조회 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
            @ApiResponse(responseCode = "11100", description = "잘못된 신탁 아이디값", content = @Content(schema = @Schema(implementation = ErrorResult.class))),
            @ApiResponse(responseCode = "11200", description = "등록된 자산 목록 없음 (가능 : 금전, 금전채권, 유가증권, 부동산)", content = @Content(schema = @Schema(implementation = ErrorResult.class))),
    })
    @GetMapping("/chartData")
    public SuccessResult<Map<String, Long>> getChartData(@RequestParam Long livingtrustId) {
        log.info("자산 차트 데이터 조회 시도 : {}", livingtrustId);
        log.info(propertyService.getAllPropertyAmountQuantity_ByType(livingtrustId).get("금전").toString());
        log.info(propertyService.getAllPropertyAmountQuantity_ByType(livingtrustId).get("금전채권").toString());
        log.info(propertyService.getAllPropertyAmountQuantity_ByType(livingtrustId).get("부동산").toString());
        log.info(propertyService.getAllPropertyAmountQuantity_ByType(livingtrustId).get("유가증권").toString());
        return BaseResponse.success(propertyService.getAllPropertyAmountQuantity_ByType(livingtrustId));
    }
}
