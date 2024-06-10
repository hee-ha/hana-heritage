package com.heeha.domain.livingTrust.dto;

import com.heeha.domain.deathNotifier.dto.DeathNotifierRegisterDto;
import com.heeha.domain.livingTrust.entity.LivingTrust;
import com.heeha.domain.postBeneficiary.dto.PostBeneficiaryRegisterDto;
import com.heeha.domain.property.dto.PropertyRegisterDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LivingTrustDoneDto {
    private Long livingTrustId;
    private String contractNumber;
    private String settlor;
    private String trustee;
    private LocalDate trustContractStartDate;
    private LocalDate trustContractEndDate;

    private boolean isApproved;
    private List<PostBeneficiaryRegisterDto> postBeneficiary;
    private List<PropertyRegisterDto> properties;
    private List<DeathNotifierRegisterDto> deathNotifiers;

    public LivingTrustDoneDto(LivingTrust livingTrust) {
        livingTrustId = livingTrust.getId();
        contractNumber = livingTrust.getContractNumber();
        settlor = livingTrust.getSettlor();
        trustee = livingTrust.getTrustee();
        trustContractStartDate = livingTrust.getTrustContractStartDate();
        trustContractEndDate = livingTrust.getTrustContractEndDate();
        postBeneficiary = livingTrust.getPostBeneficiaries().stream().map(PostBeneficiaryRegisterDto::new).toList();
        properties = livingTrust.getProperties().stream().map(PropertyRegisterDto::new).toList();
        deathNotifiers = livingTrust.getDeathNotifiers().stream().map(DeathNotifierRegisterDto::new).toList();
        isApproved = livingTrust.getIsApproved();
    }
}
