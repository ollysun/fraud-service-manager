package com.etz.fraudeagleeyemanager.repository;


import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, Long> {

}
