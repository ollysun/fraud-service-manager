package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.EmailGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailGroupRepository extends JpaRepository<EmailGroup, Long>{

}
