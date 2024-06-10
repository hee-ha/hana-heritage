package com.heeha.domain.livingTrust.service;

import com.heeha.domain.customer.entity.Customer;
import com.heeha.domain.consulting.entity.Consulting;
import com.heeha.domain.livingTrust.dto.GetLivingTrustSummaryDto;
import com.heeha.domain.customer.repository.CustomerRepository;
import com.heeha.domain.deathNotifier.dto.DeathNotifierRegisterDto;
import com.heeha.domain.deathNotifier.service.DeathNotifierService;
import com.heeha.domain.livingTrust.dto.LivingTrustCreateDto;
import com.heeha.domain.livingTrust.dto.LivingTrustDoneDto;
import com.heeha.domain.livingTrust.entity.LivingTrust;
import com.heeha.domain.livingTrust.repository.LivingTrustRepository;
import com.heeha.domain.postBeneficiary.dto.PostBeneficiaryRegisterDto;
import com.heeha.domain.postBeneficiary.service.PostBeneficiaryService;
import com.heeha.domain.property.dto.PropertyRegisterDto;
import com.heeha.domain.property.service.PropertyService;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponseStatus;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class LivingTrustService {
    private final LivingTrustRepository livingTrustRepository;
    private final CustomerRepository customerRepository;
    private final PostBeneficiaryService postBeneficiaryService;
    private final DeathNotifierService deathNotifierService;
    private final PropertyService propertyService;
    private final int LOW_BOUND = 10_000_000;
    private final int MAX_BOUND = 90_000_000;

    @Transactional
    public LivingTrustDoneDto makeContract(Long customerId, LivingTrustCreateDto livingTrustCreateDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID)
        );

        LivingTrust livingTrust = LivingTrust.builder()
                .customer(customer)
                .contractNumber(makeRandomNumber())
                .trustContractStartDate(livingTrustCreateDto.getTrustContractStartDate())
                .trustContractEndDate(livingTrustCreateDto.getTrustContractEndDate())
                .settlor(livingTrustCreateDto.getSettlor())
                .trustee(livingTrustCreateDto.getTrustee())
                .build();
        LivingTrust save = livingTrustRepository.save(livingTrust);
        log.info("유언 대용 신탁 계약 생성 완료 : {}", save.getId());
        for (PropertyRegisterDto propertyRegiDto : livingTrustCreateDto.getProperties()) {
            propertyService.save(propertyRegiDto, save);
        }
        for (PostBeneficiaryRegisterDto PostBenefitRegiDto : livingTrustCreateDto.getPostBeneficiaries()) {
            postBeneficiaryService.save(PostBenefitRegiDto, save);
        }
        for (DeathNotifierRegisterDto deathRegiDto : livingTrustCreateDto.getDeathNotifiers()) {
            deathNotifierService.save(deathRegiDto, save);
        }
        log.info("유언 대용 신탁 계약 관련 내용 등록 완료");
        return new LivingTrustDoneDto(save.getId(),save.getContractNumber(), save.getSettlor(), save.getTrustee(),
                save.getTrustContractStartDate(), save.getTrustContractEndDate(),
                false,
                livingTrustCreateDto.getPostBeneficiaries(),
                livingTrustCreateDto.getProperties(),
                livingTrustCreateDto.getDeathNotifiers());
    }

    private String makeRandomNumber() {
        Random random = new Random();
        return "C" + String.valueOf(LOW_BOUND + random.nextInt(MAX_BOUND));
    }

    public LivingTrustDoneDto getMyLivingTrust(Long customerId) {
        LivingTrust livingTrust = livingTrustRepository.findLivingTrustByCustomerId(customerId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.USERS_EMPTY_USER_ID)
        );

        return new LivingTrustDoneDto(livingTrust);
    }

    public List<GetLivingTrustSummaryDto> getAllSummary() {
        return livingTrustRepository.findAllSummary();
    }

    public List<LivingTrust> getAll() {
        return livingTrustRepository.findAll();
    }

    @Transactional
    public boolean setComplete(Long id) {
        Optional<LivingTrust> response = livingTrustRepository.findById(id);
        if (response.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_LIVING_TRUST_ID);
        }

        LivingTrust livingTrust = response.get();
        livingTrust.setApproveTrue();
        return true;
    }
}
