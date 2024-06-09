package com.heeha.domain.livingTrust.entity;

import com.heeha.domain.base.entity.BaseEntity;
import com.heeha.domain.customer.entity.Customer;
import com.heeha.domain.deathNotifier.entity.DeathNotifier;
import com.heeha.domain.postBeneficiary.entity.PostBeneficiary;
import com.heeha.domain.property.entity.Property;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "living_trust")
@Table(name = "living_trust")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LivingTrust extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "trust_contract_start_date")
    private LocalDate trustContractStartDate;

    @Column(name = "trust_contract_end_date")
    private LocalDate trustContractEndDate;

    @Column(name = "settlor", length = 50)
    private String settlor;

    @Column(name = "trustee", length = 50)
    private String trustee;

    @Column(name = "contract_number", length = 50)
    private String contractNumber;

    @OneToMany(mappedBy = "livingTrust", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Property> properties;
    @OneToMany(mappedBy = "livingTrust", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostBeneficiary> postBeneficiaries;
    @OneToMany(mappedBy = "livingTrust", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeathNotifier> deathNotifiers;

    @Column(nullable = false)
    private Boolean isApproved = Boolean.FALSE;

    public void setApproveTrue(){
        isApproved = Boolean.TRUE;
    }
}

