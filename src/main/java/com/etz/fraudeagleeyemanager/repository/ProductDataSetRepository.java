package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;

@Repository
public interface ProductDataSetRepository extends JpaRepository<ProductDataSet, ProductDatasetId> {

	List<ProductDataSet> findByProductCode(String productCode);

	//@Override
	@Query("update #{#entityName} e set e.deleted=true where e.id=?1")
	@Modifying
	@Transactional
	public void delete(@Param("id")Long id);

}
