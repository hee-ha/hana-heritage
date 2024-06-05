package com.heeha.domain.account.dto;

import com.heeha.domain.account.entity.Account;
import java.time.LocalDateTime;
import java.util.List;

import com.heeha.domain.history.dto.HistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountCheckResponse {
    private Long id;
    private Long accountNumber;
    private String name;
    private Long balance;
    private String productType;
    private LocalDateTime createdAt;
    private List<HistoryDto> histories;

    public AccountCheckResponse(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.name = account.getName();
        this.balance = account.getBalance();
        this.createdAt = account.getCreated_at();
        this.productType = accountNumber.toString().substring(3,5);
    }
}
