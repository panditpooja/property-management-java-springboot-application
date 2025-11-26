package com.mycompany.property_management.repository;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.mycompany.property_management.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    List<PropertyEntity> findByTitleContaining(String title);
//    List<PropertyEntity> findAllByUserEntityId(String userId);
//    The above commented method works. The alternative way is to use query as mentioned below.
    @Query("Select p from PropertyEntity p where p.userEntity.id = :userId")
    List<PropertyEntity> findAllByUserEntityId(@Param("userId") String userId);
    List<PropertyEntity> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
}
