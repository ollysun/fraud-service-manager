package com.etz.fraudeagleeyemanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductDatasetRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;

@Service
public class ProductService {

	@Autowired
	private ProductEntityRepository productEntityRepository;

	@Autowired
	private ProductDataSetRepository productDataSetRepository;

	@Autowired
	private ProductRedisRepository productRedisRepository;

	@Autowired
	private ProductDatasetRedisRepository productDatasetRedisRepository;

	@Autowired @Qualifier("redisTemplate")
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;

	public ProductEntity createProduct(CreateProductRequest request) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setCode(request.getProductCode());
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
		productEntity.setUseCard(request.getUseCard());
		productEntity.setUseAccount(request.getUseAccount());

		productEntity.setSupportHold(Status.ENABLED);

		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setCreatedBy(request.getCreatedBy());
		// for auditing purpose
		productEntity.setEntityId(null);
		productEntity.setRecordBefore(null);
		productEntity.setRequestDump(request);
		
		ProductEntity savedProduct = productEntityRepository.save(productEntity);
		productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRedisRepository.create(savedProduct);
		return savedProduct;
	}

	public List<ProductEntity> getProduct(String productCode) {
		if (productCode.isEmpty()) {
			return productEntityRepository.findAll();
		}

		List<ProductEntity> productList = new ArrayList<>();
		productList.add(productEntityRepository.findByCode(productCode));
		return productList;
	}

	public ProductEntity updateProduct(UpdateProductRequest request) {
		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
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

		// for auditing purpose
		productEntity.setEntityId(request.getProductCode());
		productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
		productEntity.setRequestDump(request);
		
		ProductEntity savedProduct = productEntityRepository.save(productEntity);
		productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRedisRepository.update(savedProduct);
		return savedProduct;
	}

	public Boolean deleteProduct(String productCode) {
		ProductEntity productEntity = productEntityRepository.findByCode(productCode);
		if (productEntity == null){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + productCode);
		}
		
		// for auditing purpose
		productEntity.setEntityId(productCode);
		productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
		productEntity.setRecordAfter(null);
		productEntity.setRequestDump(productCode);
		
		productEntityRepository.deleteByCode(productCode);
		productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRedisRepository.delete(productCode);
		return Boolean.TRUE;
	}

	public ProductDataSet createProductDataset(DatasetProductRequest request) {
		ProductDataSet prodDatasetEntity = new ProductDataSet();
		prodDatasetEntity.setProductCode(request.getProductCode());
		prodDatasetEntity.setFieldName(request.getFieldName());
		prodDatasetEntity.setDataType(request.getDataType());
		prodDatasetEntity.setMandatory(request.getCompulsory());
		prodDatasetEntity.setAuthorised(request.getAuthorised());
		prodDatasetEntity.setCreatedBy(request.getCreatedBy());

		ProductDataSet savedProductDataset = productDataSetRepository.save(prodDatasetEntity);
		productDatasetRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productDatasetRedisRepository.create(savedProductDataset);
		return savedProductDataset;
	}

	public List<ProductDataSet> getProductDataset(String productCode) {
		if (productCode == null) {
			return productDataSetRepository.findAll();
		}

		List<ProductDataSet> productDatasetList = new ArrayList<>();
		productDatasetList.add(productDataSetRepository.findByProductCode(productCode));
		return productDatasetList;
	}

	public ProductDataSet updateProductDataset(UpdateDataSetRequest request) {
		ProductDataSet productDatasetEntity = productDataSetRepository.findByProductCode(request.getProductCode());
		if (productDatasetEntity == null){
			throw new ResourceNotFoundException("Product dataset details not found for this code " + request.getProductCode());
		}
		productDatasetEntity.setProductCode(request.getProductCode());
		productDatasetEntity.setFieldName(request.getFieldName());
		productDatasetEntity.setDataType(request.getDataType());
		productDatasetEntity.setMandatory(request.getCompulsory());
		productDatasetEntity.setAuthorised(request.getAuthorised());
		productDatasetEntity.setUpdatedBy(request.getUpdatedBy());

		ProductDataSet savedProductDataset = productDataSetRepository.save(productDatasetEntity);
		productDatasetRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productDatasetRedisRepository.update(savedProductDataset);
		return savedProductDataset;
	}

	public boolean deleteProductDataset(String productCode) {
		ProductDataSet productDatasetEntity = productDataSetRepository.findByProductCode(productCode);
		if (productDatasetEntity == null){
			throw new ResourceNotFoundException("Product dataset details not found for this code " + productCode);
		}
		productDataSetRepository.deleteByProductCode(productCode);
		productDatasetRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productDatasetRedisRepository.delete(productDatasetEntity.getId());
		return true;
	}

}
