package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.AccountProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountProductRepository extends JpaRepository<AccountProduct, AccountProductId> {

    List<AccountProduct> findByAccountId(Long accountId);
}
