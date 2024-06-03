package com.heeha.domain.autoTransfer.entity;

import com.heeha.domain.account.entity.Account;
import com.heeha.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity(name = "auto_transfer")
@Table(name = "auto_transfer")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class AutoTransfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender", length = 100, nullable = false)
    private String sender;

    @Column(name = "recipient", length = 100)
    private String recipient;

    @Column(name = "recipien_bank", length = 100, nullable = false)
    private String recipientBank;

    @Column(name = "to_account_number", length = 13, nullable = false)
    private Long toAccountNumber;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "auto_transfer_day", nullable = false)
    private int autoTransferDay;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
