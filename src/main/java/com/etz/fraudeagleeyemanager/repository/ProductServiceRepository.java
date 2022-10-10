package com.etz.fraudeagleeyemanager.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;


@Repository
public interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, String> {

    @Query("SELECT p FROM ProductServiceEntity p WHERE p.deleted = false and p.productCode = ?1")
    Page<ProductServiceEntity> findAllByProductCode(String code, Pageable pageable);

    Page<ProductServiceEntity> findByDeleted(Boolean statusVal, Pageable pageable);

    Optional<ProductServiceEntity> findByServiceIdAndProductCode(String serviceId, String productCode);

    long countByProductCodeAndServiceName(String code, String serviceName);

    @Query("SELECT p FROM ProductServiceEntity p WHERE p.deleted = false")
    Page<ProductServiceEntity> findAllByDeletedFalse(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value="UPDATE ProductServiceEntity b SET deleted = true, status=0 WHERE b.productEntity.code = ?1")
    void deleteByProductCode(String code);

}
