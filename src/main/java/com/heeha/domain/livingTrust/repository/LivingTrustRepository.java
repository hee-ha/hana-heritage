package com.heeha.domain.livingTrust.repository;

import com.heeha.domain.livingTrust.dto.GetLivingTrustSummaryDto;
import com.heeha.domain.livingTrust.entity.LivingTrust;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivingTrustRepository extends JpaRepository<LivingTrust, Long> {
    @Query("SELECT new com.heeha.domain.livingTrust.dto.GetLivingTrustSummaryDto(id, customer.name, created_at, isApproved) " +
            "FROM living_trust")
    List<GetLivingTrustSummaryDto> findAllSummary();

}
