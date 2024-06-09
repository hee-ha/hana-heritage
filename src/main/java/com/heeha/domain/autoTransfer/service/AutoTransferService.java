package com.heeha.domain.autoTransfer.service;

import com.heeha.domain.account.dto.MakeTransactionDto;
import com.heeha.domain.account.entity.Account;
import com.heeha.domain.account.repository.AccountRepository;
import com.heeha.domain.account.service.AccountService;
import com.heeha.domain.autoTransfer.dto.CreateAutoTransferDto;
import com.heeha.domain.autoTransfer.entity.AutoTransfer;
import com.heeha.domain.autoTransfer.repository.AutoTransferRepository;
import com.heeha.domain.history.dto.TransferHistoryDto;
import com.heeha.domain.history.service.HistoryService;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoTransferService {
    private final AutoTransferRepository autoTransferRepository;
    private final AccountRepository accountRepository;
    private final AccountService transferService;

    // 자동이체 등록
    @Transactional
    public Long createAutoTransfer(CreateAutoTransferDto createAutoTransferDto) {
        Account fromAccount = accountRepository.findById(createAutoTransferDto.getAccountId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FROM_ACCOUNT));
        if (transferService.CheckAccountPassword(fromAccount.getPassword(), createAutoTransferDto.getPassword())) {
            AutoTransfer saved = autoTransferRepository.save(createAutoTransferDto.toEntity(fromAccount));
            return saved.getId();
        } else {
            throw new BaseException(BaseResponseStatus.FAIL_REGISTER_AUTOTRANSFER);
        }
    }

    // 자동이체 내역 조회
    @Transactional
    public List<CreateAutoTransferDto> getAutoTransferByDay(int day) {
            List<AutoTransfer> transfers = autoTransferRepository.findByAutoTransferDay(day);
            if (transfers.isEmpty()) {
                throw new BaseException(BaseResponseStatus.NO_AUTOTRANSFER);
            }else {
                List<CreateAutoTransferDto> autoTransferDtoList = transfers.stream()
                        .map(CreateAutoTransferDto::new)
                        .toList();
                return autoTransferDtoList;
            }
    }


}
