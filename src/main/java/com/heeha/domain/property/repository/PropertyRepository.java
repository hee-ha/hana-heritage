package com.heeha.domain.property.repository;

import com.heeha.domain.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByLivingTrust_IdAndType(Long livingTrustId, String type);
    //금전(cash), 부동산(reality), 유가증권(security), 금전채권(bond)
    @Query("SELECT p.type , SUM(p.amount * p.quantity) " +
            "FROM property p " +
            "JOIN p.livingTrust lt " +
            "WHERE lt.id = :livingTrustId " +
            "GROUP BY p.type")
    List<Object[]> findPropertiesByLivingTrust_Id(Long livingTrustId);
}
