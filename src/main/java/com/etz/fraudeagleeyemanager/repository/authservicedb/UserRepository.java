package com.etz.fraudeagleeyemanager.repository.authservicedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.authservicedb.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

//    UserEntity findByEmail(String email);
//
//    UserEntity findByUsername(String username);
//    List<UserEntity> findByStatus(Boolean statusVal);
//
//    Optional<UserEntity> findByEmailAndUsername(String email, String username);
//
//    UserEntity findByUsernameAndDeletedFalseAndStatusTrue(String username);
//    
//    Optional<UserEntity> findByPassword(String password);
//    
//    // Optional<UserEntity> findByUserId(String userId);
//    
//    @Transactional
//    @Modifying
//    @Query("Update UserEntity r Set deleted = true, status = 0 Where r.id = ?1")
//    void deleteByUserId(Long userId);

}
