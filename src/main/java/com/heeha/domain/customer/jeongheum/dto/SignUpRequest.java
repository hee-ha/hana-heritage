package com.heeha.domain.customer.jeongheum.dto;

import com.heeha.domain.auth.dto.RoleType;
import com.heeha.domain.customer.entity.Customer;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SignUpRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String identificationNumber;

    public Customer toEntity() {
        return Customer.builder()
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .identificationNumber(identificationNumber)
                .roleType(RoleType.USER).build();
    }
}
