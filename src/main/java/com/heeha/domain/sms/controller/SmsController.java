package com.heeha.domain.sms.controller;

import com.heeha.domain.sms.dto.ReserveSmsDto;
import com.heeha.domain.sms.entity.SmsReservation;
import com.heeha.domain.sms.service.SmsService;
import com.heeha.global.config.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @GetMapping("/send")
    public BaseResponse.SuccessResult<Boolean> sendSms(@RequestParam("phoneNumber") String phoneNumber) {
        return BaseResponse.success(smsService.sendCertificationMessage(phoneNumber));
    }

    @Operation(summary = "[😈Admin] 문자 예약하기")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "문자 예약 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @PostMapping("/reservation")
    public BaseResponse.SuccessResult<Boolean> reserveSms(@RequestBody ReserveSmsDto reserveDto) {
        return BaseResponse.success(smsService.reserve(reserveDto));
    }

    @Operation(summary = "[😈Admin] 문자 예약 및 발송 결과 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "문자 예약 성공", content = @Content(schema = @Schema(implementation = BaseResponse.SuccessResult.class))),
    })
    @GetMapping("/reservation/list")
    public BaseResponse.SuccessResult<List<SmsReservation>> getList() {
        return BaseResponse.success(smsService.getAll());
    }

}
