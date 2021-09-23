package com.etz.fraudeagleeyemanager.repository.authservicedb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.authservicedb.PermissionEntity;


@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByName(String name);

    List<PermissionEntity> findByStatus(Boolean status);

    long countById(Long id);

    @Transactional
    @Modifying
    @Query("Update PermissionEntity r set deleted = true, status=0 Where r.id = ?1")
    void deleteByPermissionId(Long Id);


}
