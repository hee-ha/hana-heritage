package com.heeha.domain.account.controller;

import com.heeha.domain.account.dto.AccountCheckResponse;
import com.heeha.domain.account.dto.AccountValidationRequest;
import com.heeha.domain.account.dto.MakeTransactionDto;
import com.heeha.domain.account.dto.NormalAccountCreateDto;
import com.heeha.domain.account.service.AccountService;
import com.heeha.domain.auth.Auth;
import com.heeha.global.config.BaseResponse;
import com.heeha.global.config.BaseResponse.ErrorResult;
import com.heeha.global.config.BaseResponse.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "계좌 개설")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌개설 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
    })
    @PostMapping("/create")
    public SuccessResult<AccountCheckResponse> createAccount(@Auth Long customerId,
                                                             @RequestBody NormalAccountCreateDto accountCreateDto) {
        return BaseResponse.success(
                new AccountCheckResponse(accountService.createAccount(customerId, accountCreateDto)));
    }

    @Operation(summary = "계좌 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌조회 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
    })
    @GetMapping("/my")
    public SuccessResult<List<AccountCheckResponse>> myAccounts(@Auth Long customerId) {
        return BaseResponse.success(accountService.myAccounts(customerId));
    }

    @Operation(summary = "단일 계좌 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "단일 계좌조회 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
    })
    @GetMapping("/get")
    public SuccessResult<AccountCheckResponse> getMyAccounts(@RequestParam Long accountId) {
        return BaseResponse.success(new AccountCheckResponse(accountService.getAccount(accountId)));
    }

    @Operation(summary = "계좌 이체")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "계좌이체 성공", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
            @ApiResponse(responseCode = "3300", description = "계좌이체 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class))),
    })
    @PostMapping("/simple")
    public BaseResponse.SuccessResult<MakeTransactionDto> simpleTransfer(@Auth Long customerId,
                                                                         @RequestBody MakeTransactionDto makeTransactionDto) {
        log.info("계좌이체 시도 : {}", makeTransactionDto.toString());
        return BaseResponse.success(accountService.makeTransaction(makeTransactionDto));
    }

    @Operation(summary = "계좌 비밀번호 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "비밀번호 일치", content = @Content(schema = @Schema(implementation = SuccessResult.class))),
    })
    @PostMapping("/validate")
    public SuccessResult<Boolean> validateAccount(@RequestBody AccountValidationRequest validationRequest) {
        return BaseResponse.success(accountService.validateAccount(validationRequest));
    }


}
