package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.AccountProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountProductRepository extends JpaRepository<AccountProduct, AccountProductId> {
    AccountProduct findByAccountId(Long accountId);
}
