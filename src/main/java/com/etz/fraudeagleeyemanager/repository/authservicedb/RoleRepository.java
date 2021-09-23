package com.etz.fraudeagleeyemanager.repository.authservicedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.authservicedb.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	
//	  List<Role> findByStatus(Boolean statusVal);
//	  
//	  
//	  @Transactional
//	  
//	  @Modifying
//	  
//	  @Query("Update Role r set deleted = true, status=0 Where r.id = ?1") void
//	  deleteByRoleId(Long roleId);
	 
}
