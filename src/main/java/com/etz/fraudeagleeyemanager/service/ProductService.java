package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductEntityRepository productEntityRepository;

	@Autowired
	private ProductDataSetRepository productDataSetRepository;

	public ProductEntity createProduct(CreateProductRequest request) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setCode(request.getProductCode());
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
		productEntity.setUseCard(request.getUseCard());
		productEntity.setUseAccount(request.getUseAccount());
		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setCreatedBy(request.getCreatedBy());

		return productEntityRepository.save(productEntity);
	}

	public List<ProductEntity> getProduct(String productCode) {
		if (productCode.isEmpty()) {
			return productEntityRepository.findAll();
		}

		List<ProductEntity> productList = new ArrayList<>();
		productList.add(productEntityRepository.findByProductCode(productCode));
		return productList;
	}

	public ProductEntity updateProduct(UpdateProductRequest request) {
		ProductEntity productEntity = productEntityRepository.findByProductCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product not found for Code" + request.getProductCode());
		}
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
		productEntity.setUseCard(request.getUseCard());
		productEntity.setUseAccount(request.getUseAccount());
		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setUpdatedBy(request.getUpdatedBy());
		productEntity.setUpdatedAt(LocalDateTime.now());

		return productEntityRepository.save(productEntity);
	}

	public boolean deleteProduct(String productCode) {
		// child records have to be deleted
		return productEntityRepository.deleteByProductCode(productCode);
	}

	public ProductDataSet createProductDataset(DatasetProductRequest request) {
		ProductDataSet prodDatasetEntity = new ProductDataSet();
		prodDatasetEntity.setProductCode(request.getProductCode());
		prodDatasetEntity.setFieldName(request.getFieldName());
		prodDatasetEntity.setDataType(request.getDataType());
		prodDatasetEntity.setMandatory(request.getCompulsory());
		prodDatasetEntity.setAuthorised(request.getAuthorised());
		prodDatasetEntity.setCreatedBy(request.getCreatedBy());

		return productDataSetRepository.save(prodDatasetEntity);
	}

	public List<ProductDataSet> getProductDataset(String productCode) {
		if (productCode.isEmpty()) {
			return productDataSetRepository.findAll();
		}

		List<ProductDataSet> productDatasetList = new ArrayList<>();
		productDatasetList.add(productDataSetRepository.findByProductCode(productCode));
		return productDatasetList;
	}

	public ProductDataSet updateProductDataset(UpdateDataSetRequest request) {
		ProductDataSet productDatasetEntity = getProductDataset(request.getProductCode()).get(0);
		productDatasetEntity.setProductCode(request.getProductCode());
		productDatasetEntity.setFieldName(request.getFieldName());
		productDatasetEntity.setDataType(request.getDataType());
		productDatasetEntity.setMandatory(request.getCompulsory());
		productDatasetEntity.setAuthorised(request.getAuthorised());
		productDatasetEntity.setUpdatedBy(request.getUpdatedBy());
		return productDataSetRepository.save(productDatasetEntity);
	}

	public boolean deleteProductDataset(String productCode) {
		return productDataSetRepository.deleteByProductCode(productCode);
	}

}
