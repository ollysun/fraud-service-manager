package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDataSetRepository extends JpaRepository<ProductDataSet, ProductDatasetId> {

	ProductDataSet findByProductCode(String productCode);
	
	Boolean deleteByProductCode(String productCode);
}
