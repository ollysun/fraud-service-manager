package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductDataSetRepository extends JpaRepository<ProductDataSet, ProductDatasetId> {

	ProductDataSet findByProductCode(String productCode);

	//@SQLDelete(sql = "UPDATE product_dataset SET deleted = true WHERE product_code = ?", check = ResultCheckStyle.COUNT)

	//@Query("update #{#entityName} e set e.deleted=true where e.productCode=?1")
	@Transactional
	@Query("update #{#entityName} e set e.deleted=true where e.id=?1")
	@Modifying
	void deleteById(Long id);

}
