package com.mycompany.property_management.repository;

import com.mycompany.property_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByOwnerEmail(String email);
//    List<PropertyEntity> findByPriceGreaterThanEqualAndPriceLessThanEqual(Double minPrice, Double maxPrice);
}
