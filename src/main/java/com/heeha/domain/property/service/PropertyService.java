package com.heeha.domain.property.service;

import com.heeha.domain.livingTrust.entity.LivingTrust;
import com.heeha.domain.property.dto.PropertyRegisterDto;
import com.heeha.domain.property.dto.PropertyResponse;
import com.heeha.domain.property.entity.Property;
import com.heeha.domain.property.repository.PropertyRepository;
import com.heeha.global.config.BaseException;
import com.heeha.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    @Transactional
    public Map<String, List<PropertyResponse>> getAllProperties(Long livingTrustId) {

        Map<String, List<PropertyResponse>> propertiesMap = new HashMap<>();
        propertiesMap.put("cash", getCashProperty(livingTrustId));
        propertiesMap.put("security", getSecurityProperty(livingTrustId));
        propertiesMap.put("realty", getRealtyProperty(livingTrustId));
        propertiesMap.put("bond", getBondProperty(livingTrustId));
        return propertiesMap;
    }
    @Transactional
    public Long save(PropertyRegisterDto propertyDto, LivingTrust livingTrust) {
        Property property = Property.builder()
                .livingTrust(livingTrust)
                .type(propertyDto.getPropertyType())
                .amount(propertyDto.getAmount())
                .location(propertyDto.getLocation())
                .quantity(propertyDto.getQuantity())
                .name(propertyDto.getName())
                .build();

        return propertyRepository.save(property).getId();
    }
    @Transactional
    public List<PropertyResponse> getCashProperty(Long livingTrustId) {
        try {
            List<Property> properties = propertyRepository.findAllByLivingTrust_IdAndType(livingTrustId, "금전");
            return properties.stream().map(PropertyResponse::new).toList();
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.INVAILD_LIVINGTRUSTID);
        }
    }
    @Transactional
    public List<PropertyResponse> getSecurityProperty(Long livingTrustId) {
        try {
            List<Property> properties = propertyRepository.findAllByLivingTrust_IdAndType(livingTrustId, "유가증권");
            return properties.stream().map(PropertyResponse::new).toList();
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.INVAILD_LIVINGTRUSTID);
        }
    }
    @Transactional
    public List<PropertyResponse> getRealtyProperty(Long livingTrustId) {
        try {
            List<Property> properties = propertyRepository.findAllByLivingTrust_IdAndType(livingTrustId, "부동산");
            return properties.stream().map(PropertyResponse::new).toList();
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.INVAILD_LIVINGTRUSTID);
        }
    }

    @Transactional
    public List<PropertyResponse> getBondProperty(Long livingTrustId) {
        try {
            List<Property> properties = propertyRepository.findAllByLivingTrust_IdAndType(livingTrustId, "금전채권");
            return properties.stream().map(PropertyResponse::new).toList();
        }  catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.INVAILD_LIVINGTRUSTID);
        }
    }
    @Transactional
    public Map<String, Long> getAllPropertyAmountQuantity_ByType(Long livingTrustId) {
        try {
                List<Object[]> results = propertyRepository.findPropertiesByLivingTrust_Id(livingTrustId);
                return results.stream()
                        .collect(Collectors.toMap(
                                result -> (String) result[0],
                                result -> (Long) result[1]
                        ));


            //return propertyRepository.findPropertiesByLivingTrust_Id(livingTrustId);
        }  catch (DataIntegrityViolationException e) {
            throw new BaseException(BaseResponseStatus.INVAILD_LIVINGTRUSTID);
        }
    }
}