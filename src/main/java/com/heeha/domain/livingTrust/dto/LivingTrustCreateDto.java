package com.heeha.domain.livingTrust.dto;

import com.heeha.domain.deathNotifier.dto.DeathNotifierRegisterDto;
import com.heeha.domain.postBeneficiary.dto.PostBeneficiaryRegisterDto;
import com.heeha.domain.property.dto.PropertyRegisterDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class LivingTrustCreateDto {
    private LocalDate trustContractStartDate;
    private LocalDate trustContractEndDate;

    private String settlor;
    private String trustee;

    private List<PropertyRegisterDto> properties;
    private List<PostBeneficiaryRegisterDto> postBeneficiaries;
    private List<DeathNotifierRegisterDto> deathNotifiers;

}
