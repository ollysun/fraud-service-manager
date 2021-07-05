package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;

@Repository
public interface ProductDataSetRepository extends JpaRepository<ServiceDataSet, ProductDatasetId> {

	List<ServiceDataSet> findByProductCode(String productCode);
	List<ServiceDataSet> findByServiceId(String serviceId);

	//@Override
	@Query("update #{#entityName} e set e.deleted=true where e.serviceId=?1")
	@Modifying
	@Transactional
	void delete(@Param("serviceId")String serviceId);

}
