package com.heeha.domain.deathNotifier.entity;

import com.heeha.domain.base.entity.BaseEntity;
import com.heeha.domain.livingTrust.entity.LivingTrust;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

import java.time.LocalDateTime;


@Entity(name = "death_notifier")
@Table(name = "death_notifier")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Getter
public class DeathNotifier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthdate;
    private String relation;

    @ManyToOne
    @JoinColumn(name = "living_trust_id")
    private LivingTrust livingTrust;
}
