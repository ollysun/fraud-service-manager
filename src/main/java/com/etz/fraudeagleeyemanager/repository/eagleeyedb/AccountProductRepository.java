package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.AccountProductId;

import java.util.List;

@Repository
public interface AccountProductRepository extends JpaRepository<AccountProduct, AccountProductId> {
    List<AccountProduct> findByAccountId(Long accountId);
}
