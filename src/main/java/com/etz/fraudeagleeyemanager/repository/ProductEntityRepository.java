package com.etz.fraudeagleeyemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, String> {

    @Query("SELECT p FROM ProductEntity p WHERE p.deleted = false and p.code = ?1")
    Optional<ProductEntity> findByCodeAndDeletedFalse(String code);

    @Query("SELECT COUNT(p.code) FROM ProductEntity p WHERE p.code = ?1")
    long findCountByCode(String code);

    @Query("SELECT p FROM ProductEntity p WHERE p.deleted = false")
    List<ProductEntity> findAllByDeletedFalse();

}
