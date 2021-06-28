package com.etz.fraudeagleeyemanager.repository;


import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, Long> {

    @Query("SELECT p FROM ProductServiceEntity p WHERE p.deleted = false and p.productCode = ?1")
    Page<ProductServiceEntity> findAllByProductCode(String code, Pageable pageable);

    Page<ProductServiceEntity> findByDeleted(Boolean statusVal, Pageable pageable);

    long countByProductCodeAndServiceName(String code, String serviceName);

    @Query("SELECT p FROM ProductServiceEntity p WHERE p.deleted = false")
    Page<ProductServiceEntity> findAllByDeletedFalse(Pageable pageable);

    @Modifying
    @Transactional
    //@Query("DELETE FROM ProductServiceEntity b WHERE b.productEntity.code = ?1")
    @Query(value="UPDATE ProductServiceEntity b SET deleted = true, status=0 WHERE b.productEntity.code = ?1")
    void deleteByProductCode(String code);

}
