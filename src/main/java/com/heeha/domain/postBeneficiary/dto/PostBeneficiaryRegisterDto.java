package com.heeha.domain.postBeneficiary.dto;

import com.heeha.domain.postBeneficiary.entity.PostBeneficiary;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostBeneficiaryRegisterDto {
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthdate;
    private String relation;
    private String ratio;

    public PostBeneficiaryRegisterDto(PostBeneficiary  postBeneficiary) {
        this.name = postBeneficiary.getName();
        this.phoneNumber = postBeneficiary.getPhoneNumber();
        this.address = postBeneficiary.getAddress();
        this.birthdate = postBeneficiary.getBirthdate();
        this.relation = postBeneficiary.getRelation();
        this.ratio = postBeneficiary.getRatio();
    }
}
