package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductDataset;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDatasetRepository extends JpaRepository<ProductDataset, ProductDatasetId> {

	//ProductDataset findByProductCode(String productCode);
	
	//void deleteByProductCode(String productCode);
}
