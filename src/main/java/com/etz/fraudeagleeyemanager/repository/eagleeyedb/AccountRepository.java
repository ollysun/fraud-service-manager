package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

    Optional<Account> findByAccountNo(String accountNumber);
}
