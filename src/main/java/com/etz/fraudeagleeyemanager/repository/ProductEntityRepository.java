package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByCode(String code);

    Boolean deleteByCode(String code);
}
