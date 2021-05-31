package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.ProductDataSetResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductResponse;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductDatasetRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private final ProductEntityRepository productEntityRepository;

	@Autowired
	private final ProductDataSetRepository productDataSetRepository;

	@Autowired
	private final ProductRedisRepository productRedisRepository;

	@Autowired
	private final ProductDatasetRedisRepository productDatasetRedisRepository;

	@Autowired @Qualifier("redisTemplate")
	private final RedisTemplate<String, Object> fraudEngineRedisTemplate;

	public ProductService(ProductEntityRepository productEntityRepository, ProductDataSetRepository productDataSetRepository, ProductRedisRepository productRedisRepository, ProductDatasetRedisRepository productDatasetRedisRepository, RedisTemplate<String, Object> fraudEngineRedisTemplate) {
		this.productEntityRepository = productEntityRepository;
		this.productDataSetRepository = productDataSetRepository;
		this.productRedisRepository = productRedisRepository;
		this.productDatasetRedisRepository = productDatasetRedisRepository;
		this.fraudEngineRedisTemplate = fraudEngineRedisTemplate;
	}

	public ProductResponse createProduct(CreateProductRequest request) {
		ProductEntity productEntity = new ProductEntity();
		try {
			productEntity.setCode(request.getProductCode());
			productEntity.setName(request.getProductName());
			productEntity.setDescription(request.getProductDesc());
			productEntity.setUseCard(request.getUseCard());
			productEntity.setUseAccount(request.getUseAccount());
			productEntity.setCallbackURL(request.getCallback());
			productEntity.setSupportHold(Boolean.FALSE);
			productEntity.setStatus(Boolean.TRUE);
			productEntity.setCreatedBy(request.getCreatedBy());

			ProductEntity savedProduct = productEntityRepository.save(productEntity);
			productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
			productRedisRepository.create(savedProduct);

			return outputProductResponse(savedProduct);
		}catch (Exception ex){
			throw new FraudEngineException(ex.getMessage());
		}
	}

	private ProductResponse outputProductResponse(ProductEntity productEntity){
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(productEntity,productResponse,"productDataset, productRules, products, productLists");
		return productResponse;
	}

	public List<ProductResponse>  getProduct(String productCode) {
		List<ProductResponse> productResponseList;
		if (productCode == null) {
			return outputCreateProduct(productEntityRepository.findAll());
		}

		List<ProductEntity> productList = new ArrayList<>();
		productList.add(productEntityRepository.findByCode(productCode));
		productResponseList = outputCreateProduct(productList);
		return productResponseList;
	}

	private List<ProductResponse> outputCreateProduct(List<ProductEntity> productEntityList){
		List<ProductResponse> productResponseList = new ArrayList<>();
		productEntityList.forEach(productVal -> {
				ProductResponse productResponse = new ProductResponse();
			    BeanUtils.copyProperties(productVal,productResponse,"productDataset, productRules, products, productLists");
				productResponseList.add(productResponse);
		});
		return productResponseList;
	}

	public ProductEntity updateProduct(UpdateProductRequest request) {
		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product not found for Code " + request.getProductCode());
		}
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
		productEntity.setUseCard(request.getUseCard());
		productEntity.setUseAccount(request.getUseAccount());
		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setUpdatedBy(request.getUpdatedBy());

		ProductEntity savedProduct = productEntityRepository.save(productEntity);
		productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRedisRepository.update(savedProduct);
		return outputUpdatedProductResponse(savedProduct);
	}

	private ProductResponse outputUpdatedProductResponse(ProductEntity productEntity){
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(productEntity,productResponse,"createdBy, createdAt,productDataset, productRules, products, productLists");
		return productResponse;
	}

	public Boolean deleteProduct(String productCode) {
		ProductEntity productEntity = productEntityRepository.findByCode(productCode);
		if (productEntity == null){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + productCode);
		}
		productEntityRepository.deleteById(productCode);
		productRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRedisRepository.delete(productCode);
		return Boolean.TRUE;
	}

	public ProductDataSet createProductDataset(DatasetProductRequest request) {
		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product not found for Code " + request.getProductCode());
		}
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
		return outputCreatedProductDataset(savedProductDataset);
	}

	private ProductDataSetResponse outputCreatedProductDataset(ProductDataSet productEntity){
		ProductDataSetResponse productResponse = new ProductDataSetResponse();
		BeanUtils.copyProperties(productEntity,productResponse,"productEntity");
		return productResponse;
	}

	public List<ProductDataSetResponse> getProductDataset(String productCode) {
		List<ProductDataSet> productDatasetList = new ArrayList<>();
		if (productCode == null) return outputProductDatasetEntity(productDataSetRepository.findAll());
		productDatasetList.add(productDataSetRepository.findByProductCode(productCode));
		return outputProductDatasetEntity(productDatasetList);
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
		return outputUpdatedProductDatasetResponse(savedProductDataset);
	}

	private ProductDataSetResponse outputUpdatedProductDatasetResponse(ProductDataSet productEntity){
		ProductDataSetResponse productResponse = new ProductDataSetResponse();
		BeanUtils.copyProperties(productEntity,productResponse,"createdBy, createdAt,productDataset, productRules, products, productLists");
		return productResponse;
	}

	public boolean deleteProductDataset(String productCode) {
		ProductDataSet productDatasetEntity = productDataSetRepository.findByProductCode(productCode);
		if (productDatasetEntity == null){
			throw new ResourceNotFoundException("Product dataset details not found for this code " + productCode);
		}
		productDataSetRepository.deleteById(productDatasetEntity.getId());
		productDatasetRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productDatasetRedisRepository.delete(productDatasetEntity.getId());
		return true;
	}

	private List<ProductDataSetResponse> outputProductDatasetEntity(List<ProductDataSet> productDatasetList){
		List<ProductDataSetResponse> productResponseList = new ArrayList<>();
		productDatasetList.forEach(productVal -> {
			ProductDataSetResponse productResponse = new ProductDataSetResponse();
			BeanUtils.copyProperties(productVal,productResponse,"productEntity");
			productResponseList.add(productResponse);
		});
		return productResponseList;
	}

}
