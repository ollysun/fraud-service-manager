package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.AccountProductId;

@Repository
public interface AccountProductRepository extends JpaRepository<AccountProduct, AccountProductId> {
    List<AccountProduct> findByAccountId(Long accountId);
    
    List<AccountProduct> findByProductCode(String productCode);
}
