package com.mycompany.property_management.repository;

import com.mycompany.property_management.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    List<PropertyEntity> findByTitleContaining(String title);
    List<PropertyEntity> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
}
