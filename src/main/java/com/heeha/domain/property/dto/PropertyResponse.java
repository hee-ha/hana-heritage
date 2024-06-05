package com.heeha.domain.property.dto;

import com.heeha.domain.livingTrust.entity.LivingTrust;
import com.heeha.domain.property.entity.Property;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PropertyResponse {
    //private Long id;

//    private LivingTrust livingTrust;

    private String type;

    private Long amount;

    private String location;

    private Integer quantity;

    public PropertyResponse(Property property) {
        //this.id = property.getId();
//        this.livingTrust = property.getLivingTrust();
        this.type = property.getType();
        this.amount = property.getAmount();
        this.location = property.getLocation();
        this.quantity = property.getQuantity();
    }
}
