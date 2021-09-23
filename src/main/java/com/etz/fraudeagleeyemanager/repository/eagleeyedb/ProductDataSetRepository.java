package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ProductDatasetId;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ServiceDataSet;

@Repository
public interface ProductDataSetRepository extends JpaRepository<ServiceDataSet, ProductDatasetId> {

	List<ServiceDataSet> findByProductCode(String productCode);
	List<ServiceDataSet> findByServiceId(String serviceId);


	@Query("SELECT p FROM ServiceDataSet p WHERE p.datasetId = ?1 and p.productCode = ?2 and p.serviceId = ?3")
	Optional<ServiceDataSet> findByIds(Long id, String code, String serviceId);

	//@Override
	@Query("Update #{#entityName} e set e.deleted=true where e.datasetId = ?1 and e.productCode = ?2 and e.serviceId=?3")
	@Modifying
	@Transactional
	void delete(@Param("datasetId")Long datasetId,
				@Param("productCode")String productCode,
				@Param("serviceId")String serviceId);

}
