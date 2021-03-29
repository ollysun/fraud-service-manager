package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDatasetRepository extends JpaRepository<ProductDataset, Long> {

	ProductDataset findByProductCode(String productCode);
	
	void deleteByProductCode(String productCode);
}
