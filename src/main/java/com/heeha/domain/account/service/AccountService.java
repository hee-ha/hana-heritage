package com.heeha.domain.account.service;

import com.heeha.domain.account.dto.AccountCheckResponse;
import com.heeha.domain.account.dto.AccountCreateDto;
import com.heeha.domain.account.dto.AccountValidationRequest;
import com.heeha.domain.account.dto.MakeTransactionDto;
import com.heeha.domain.account.repository.AccountRepository;
import com.heeha.domain.account.entity.Account;
import com.heeha.domain.customer.entity.Customer;
import com.heeha.domain.customer.repository.CustomerRepository;
import com.heeha.domain.history.dto.TransferHistoryDto;
import com.heeha.domain.history.service.HistoryService;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponseStatus;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final HistoryService historyService;
    private final int LOW_BOUND = 10_000_000;
    private final int MAX_BOUND = 90_000_000;

    @Transactional
    public Account createAccount(Long customerId, AccountCreateDto accountCreateDto) {
        Long accountNumber;
        do {
            accountNumber = generateAccountNumber(accountCreateDto.getBranchCode(), accountCreateDto.getAccountCode());
        } while (accountRepository.existsAccountByAccountNumber(accountNumber));

        Customer customer = customerRepository.findById(customerId).get();
        Account account = accountCreateDto.toEntity(accountNumber, customer);

        return accountRepository.save(account);
    }

    public List<AccountCheckResponse> myAccounts(Long customerId) {
        List<Account> accounts = accountRepository.findAccountByCustomerId(customerId);
        return accounts.stream().map(AccountCheckResponse::new).toList();
    }

    public Boolean validateAccount(AccountValidationRequest validationRequest) {
        Account account = accountRepository.findById(validationRequest.getAccountId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SYSTEM_ERROR));

        if (!account.getPassword().equals(validationRequest.getAccountPassword())) {
            throw new BaseException(BaseResponseStatus.INVALID_ACCOUNT_PASSWORD);
        }
        return Boolean.TRUE;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.SYSTEM_ERROR)
        );
    }

    @Transactional
    public MakeTransactionDto makeTransaction(MakeTransactionDto makeTransactionDto) {
        // 계좌 아이디 기준으로 출금 계좌 정보 조회
        Account account = accountRepository.findById(makeTransactionDto.getAccountId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FROM_ACCOUNT));

        // 잔액 조회 - 이체 가능 여부 확인
        checkBalance(account.getBalance(), makeTransactionDto.getAmount());

        // 비밀번호 일치 여부 확인
        if (CheckAccountPassword(account.getPassword(), makeTransactionDto.getPassword())) {
            String recipient = null;
            // 하나 -> 하나 이체
            if (makeTransactionDto.getRecipientBank().equals("하나")) {
                Account toAccount = accountRepository.findByAccountNumber(
                                makeTransactionDto.getRecipientAccountNumber())
                        .orElseThrow(
                                () -> new BaseException(BaseResponseStatus.NO_TO_ACCOUNT)
                        );

                recipient = toAccount.getCustomer().getName();
                //당행 이체 처리
                hanaTransfer(account, toAccount, makeTransactionDto.getAmount());
                historyService.historySave(TransferHistoryDto.builder()
                        .dealClassification("입금")
                        .amount(makeTransactionDto.getAmount())
                        .recipientBank(makeTransactionDto.getRecipientBank())
                        .senderNumber(account.getAccountNumber())
                        .recipientRemarks(makeTransactionDto.getSenderRemarks())
                        .sender(account.getCustomer().getName())
                        .senderRemarks(makeTransactionDto.getRecipientRemarks())
                        .account(toAccount).build());

            }
            // 타행이체
            else {
                otherTransfer(account, makeTransactionDto.getAmount());
            }
            historyService.historySave(TransferHistoryDto.builder()
                    .dealClassification("출금")
                    .amount(makeTransactionDto.getAmount())
                    .recipient(recipient)
                    .recipientBank(makeTransactionDto.getRecipientBank())
                    .recipientNumber(makeTransactionDto.getRecipientAccountNumber())
                    .recipientRemarks(makeTransactionDto.getRecipientRemarks())
                    .senderRemarks(makeTransactionDto.getSenderRemarks())
                    .account(account).build());
        }
        return makeTransactionDto;
    }

    @Transactional
    public void hanaTransfer(Account fromAccount, Account toAccount, Long amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    @Transactional
    public void otherTransfer(Account fromAccount, Long amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
    }

    public void checkBalance(Long balance, long amount) {
        // 잔액이 부족한 경우 -> 메서드 분리
        if (balance < amount) {
            throw new BaseException(BaseResponseStatus.INVALID_BALANCE);
        }
    }


    public boolean CheckAccountPassword(String accPw, String inputPw) {
        if (!accPw.equals(inputPw)) {
            throw new BaseException(BaseResponseStatus.WRONG_PASSWORD);
        }
        return true;
    }

    private Long generateAccountNumber(String branchCode, String accountCode) {
        StringBuilder sb = new StringBuilder(branchCode).append(accountCode).append(makeRandomNumber());
        return Long.parseLong(sb.toString());
    }

    private String makeRandomNumber() {
        Random random = new Random();
        return String.valueOf(LOW_BOUND + random.nextInt(MAX_BOUND));
    }
}
