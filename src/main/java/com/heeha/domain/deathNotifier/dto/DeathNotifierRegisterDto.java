package com.heeha.domain.deathNotifier.dto;

import com.heeha.domain.deathNotifier.entity.DeathNotifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeathNotifierRegisterDto {
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthdate;
    private String relation;

    public DeathNotifierRegisterDto(DeathNotifier deathNotifier) {
        this.name = deathNotifier.getName();
        this.phoneNumber = deathNotifier.getPhoneNumber();
        this.address = deathNotifier.getAddress();
        this.birthdate = deathNotifier.getBirthdate();
        this.relation = deathNotifier.getRelation();
    }
}
