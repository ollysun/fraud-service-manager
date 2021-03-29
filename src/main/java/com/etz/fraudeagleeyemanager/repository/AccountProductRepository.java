package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountProductRepository extends JpaRepository<AccountProduct, Long> {

}
