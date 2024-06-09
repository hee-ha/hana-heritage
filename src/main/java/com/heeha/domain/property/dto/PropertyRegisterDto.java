package com.heeha.domain.property.dto;

import com.heeha.domain.property.entity.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PropertyRegisterDto {
    private String propertyType;
    private Long amount;
    private String location;
    private Integer quantity;
    private String name;

    public PropertyRegisterDto(Property property) {
        this.propertyType = property.getType();
        this.amount = property.getAmount();
        this.location = property.getLocation();
        this.quantity = property.getQuantity();
        this.name = property.getName();
    }
}
