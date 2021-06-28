package com.etz.fraudeagleeyemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.etz.fraudeagleeyemanager.dto.request.*;
import com.etz.fraudeagleeyemanager.dto.response.ProductServiceResponse;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.repository.ProductServiceRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
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
import com.etz.fraudeagleeyemanager.util.JsonConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ProductEntityRepository productEntityRepository;
	private final ProductDataSetRepository productDataSetRepository;
	private final ProductRedisRepository productRedisRepository;
	private final ProductDatasetRedisRepository productDatasetRedisRepository;
	private final ProductServiceRepository productServiceRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@CacheEvict(value = "product", allEntries=true)
	public ProductResponse createProduct(CreateProductRequest request) {
		ProductEntity productEntity = new ProductEntity();
		if (productEntityRepository.findCountByCode(request.getProductCode()) > 0){
			throw new FraudEngineException("Similar record already exist for code " + request.getProductCode());
		}
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

			// for auditing purpose for CREATE
			productEntity.setEntityId(null);
			productEntity.setRecordBefore(null);
			productEntity.setRequestDump(request);


		} catch (Exception ex) {
			log.error("Error occurred while creating product entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputProductResponse(saveProductEntityToDatabase(productEntity));
	}

	private ProductEntity saveProductEntityToDatabase(ProductEntity accountEntity) {
		ProductEntity persistedProductEntity;
		try {
			persistedProductEntity = productEntityRepository.save(accountEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving product entity to database", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveProductEntityToRedis(persistedProductEntity);
		return persistedProductEntity;
	}

	private void saveProductEntityToRedis(ProductEntity alreadyPersistedProductEntity) {
		try {
			productRedisRepository.setHashOperations(redisTemplate);
			productRedisRepository.update(alreadyPersistedProductEntity);
		} catch (Exception ex) {
			// TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving product entity to Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	private ProductResponse outputProductResponse(ProductEntity productEntity) {
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(productEntity, productResponse,
				"productDataset, productRules, products, productLists");
		return productResponse;
	}

	@Transactional(readOnly=true)
	@Cacheable(value="product")
	public List<ProductResponse> getProduct(String productCode) {
		if (Objects.isNull(productCode)) {
			return outputCreateProduct(productEntityRepository.findAllByDeletedFalse());
		}
		Optional<ProductEntity> productEntityOptional = findByCode(productCode);
		List<ProductEntity> productList = new ArrayList<>();
		productEntityOptional.ifPresent(productList::add);
		return outputCreateProduct(productList);
	}

	private List<ProductResponse> outputCreateProduct(List<ProductEntity> productEntityList) {
		List<ProductResponse> productResponseList = new ArrayList<>();
		productEntityList.forEach(productVal -> {
			ProductResponse productResponse = new ProductResponse();
			BeanUtils.copyProperties(productVal, productResponse,
					"productDataset, productRules, products, productLists");
			productResponseList.add(productResponse);
		});
		return productResponseList;
	}

	@Transactional
	@CacheEvict(value = "product", allEntries=true)
	public ProductResponse updateProduct(UpdateProductRequest request) {
		Optional<ProductEntity> productEntityOptional = findByCode(request.getProductCode());
		ProductEntity productEntity = productEntityOptional.get();

		try {
			// for auditing purpose for UPDATE
			productEntity.setEntityId(request.getProductCode());
			productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
			productEntity.setRequestDump(request);

			productEntity.setName(request.getProductName());
			productEntity.setDescription(request.getProductDesc());
			productEntity.setUseCard(request.getUseCard());
			productEntity.setUseAccount(request.getUseAccount());
			productEntity.setCallbackURL(request.getCallback());
			productEntity.setStatus(request.getStatus());
			productEntity.setUpdatedBy(request.getUpdatedBy());
		} catch (Exception ex) {
			log.error("Error occurred while creating product entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputUpdatedProductResponse(saveProductEntityToDatabase(productEntity));
	}

	private ProductResponse outputUpdatedProductResponse(ProductEntity productEntity) {
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(productEntity, productResponse,
				"createdBy, createdAt,productDataset, productRules, products, productLists");
		return productResponse;
	}

	@Transactional
	@CacheEvict(value = "product")
	public Boolean deleteProduct(String productCode) {
		Optional<ProductEntity> productEntityOptional = findByCode(productCode);
		ProductEntity productEntity = productEntityOptional.get();

		// for auditing purpose for DELETE
		productEntity.setEntityId(productCode);
		productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
		productEntity.setRecordAfter(null);
		productEntity.setRequestDump(productCode);

		try {
			productEntityRepository.delete(productEntity);
		} catch (Exception ex) {
			log.error("Error occurred while deleting product entity from database", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			productRedisRepository.setHashOperations(redisTemplate);
			productRedisRepository.delete(productCode);
		} catch (Exception ex) {
			log.error("Error occurred while deleting product entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		return Boolean.TRUE;
	}

	private Optional<ProductEntity> findByCode(String productCode) {
		Optional<ProductEntity> productEntityOptional = productEntityRepository.findByCodeAndDeletedFalse(productCode);
		if (!productEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Product not found for Product Code " + productCode);
		}
		return productEntityOptional;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ProductDataSet createProductDataset(DatasetProductRequest request) {
		//check for the code before creating the dataset
		findByCode(request.getProductCode());

		ProductDataSet prodDatasetEntity = new ProductDataSet();
		try {
			prodDatasetEntity.setProductCode(request.getProductCode());
			prodDatasetEntity.setServiceId(request.getServiceId());
			prodDatasetEntity.setFieldName(request.getFieldName());
			prodDatasetEntity.setDataType(request.getDataType());
			prodDatasetEntity.setMandatory(request.getCompulsory());
			prodDatasetEntity.setAuthorised(request.getAuthorised());
			prodDatasetEntity.setCreatedBy(request.getCreatedBy());

			// for auditing purpose for CREATE
			prodDatasetEntity.setEntityId(null);
			prodDatasetEntity.setRecordBefore(null);
			prodDatasetEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating product dataset entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputCreatedProductDataset(saveProductDatasetEntityToDatabase(prodDatasetEntity));
	}

	private ProductDataSetResponse outputCreatedProductDataset(ProductDataSet productEntity) {
		ProductDataSetResponse productResponse = new ProductDataSetResponse();
		BeanUtils.copyProperties(productEntity, productResponse, "productEntity");
		return productResponse;
	}
	@Transactional(readOnly = true)
	public List<ProductDataSetResponse> getProductDataset(String productCode) {
		if (Objects.isNull(productCode)) {
			return outputProductDatasetEntity(productDataSetRepository.findAll());
		}
	
		List<ProductDataSet> productDatasetList = new ArrayList<>(productDataSetRepository.findByProductCode(productCode));
		return outputProductDatasetEntity(productDatasetList);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ProductDataSetResponse> updateProductDataset(UpdateDataSetRequest request) {
		List<ProductDataSet> productDataSetList = productDataSetRepository.findByProductCode(request.getProductCode());
		if (productDataSetList.isEmpty()) {
			throw new ResourceNotFoundException("Product dataset details not found for this code " + request.getProductCode());
		}

		List<ProductDataSet> updatedDatasetList = new ArrayList<>();
		productDataSetList.forEach(productDataSet -> {
			try {
				// for auditing purpose for UPDATE
				productDataSet.setEntityId(request.getProductCode());
				productDataSet.setRecordBefore(JsonConverter.objectToJson(productDataSet));
				productDataSet.setRequestDump(request);

				productDataSet.setDataType(request.getDataType());
				productDataSet.setMandatory(request.getCompulsory());
				productDataSet.setAuthorised(request.getAuthorised());
				productDataSet.setUpdatedBy(request.getUpdatedBy());
			} catch (Exception ex) {
				log.error("Error occurred while creating product dataset entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			updatedDatasetList.add(saveProductDatasetEntityToDatabase(productDataSet));
		});
		return outputUpdatedProductDatasetResponse(updatedDatasetList);
	}

	private List<ProductDataSetResponse> outputUpdatedProductDatasetResponse(List<ProductDataSet> productDataSetList) {
		List<ProductDataSetResponse> updatedProductDatasetResponseList = new ArrayList<>();
		productDataSetList.forEach(productDataSet -> {
			ProductDataSetResponse productDataSetResponse = new ProductDataSetResponse();
			BeanUtils.copyProperties(productDataSet, productDataSetResponse, "createdBy", "createdAt", "productEntity");
			updatedProductDatasetResponseList.add(productDataSetResponse);
		});
		return updatedProductDatasetResponseList;
	}

	@Transactional
	public boolean deleteProductDataset(String productCode) {
		List<ProductDataSet> productDatasetEntityList = productDataSetRepository.findByProductCode(productCode);
		if (productDatasetEntityList.isEmpty()) {
			throw new ResourceNotFoundException("Product dataset details not found for this code " + productCode);
		}
		productDatasetRedisRepository.setHashOperations(redisTemplate);
		productDatasetEntityList.forEach(productDataset -> {
			// for auditing purpose for DELETE
			productDataset.setEntityId(productCode);
			productDataset.setRecordBefore(JsonConverter.objectToJson(productDataset));
			productDataset.setRecordAfter(null);
			productDataset.setRequestDump(productCode);

			try {
				productDataSetRepository.delete(productCode);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product dataset entity from database", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
			}
			try {
				String redisId = productDataset.getProductCode() + ":" + productDataset.getId();
				productDatasetRedisRepository.delete(redisId);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product dataset entity from Redis", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
			}
		});
		return Boolean.TRUE;
	}

	private List<ProductDataSetResponse> outputProductDatasetEntity(List<ProductDataSet> productDatasetList) {
		List<ProductDataSetResponse> productResponseList = new ArrayList<>();
		productDatasetList.forEach(productVal -> {
			ProductDataSetResponse productResponse = new ProductDataSetResponse();
			BeanUtils.copyProperties(productVal, productResponse, "productEntity");
			productResponseList.add(productResponse);
		});
		return productResponseList;
	}

	
	private ProductDataSet saveProductDatasetEntityToDatabase(ProductDataSet accountEntity) {
		ProductDataSet persistedProductDatasetEntity;
		try {
			persistedProductDatasetEntity = productDataSetRepository.save(accountEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving product entity to database", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveProductDatasetEntityToRedis(persistedProductDatasetEntity);
		return persistedProductDatasetEntity;
	}

	private void saveProductDatasetEntityToRedis(ProductDataSet alreadyPersistedProductDatasetEntity) {
		try {
			productDatasetRedisRepository.setHashOperations(redisTemplate);
			productDatasetRedisRepository.update(alreadyPersistedProductDatasetEntity);
		} catch (Exception ex) {
			// TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving product entity to Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ProductServiceResponse createProductService(CreateProductServiceDto request) {
		Optional<ProductEntity> productEntityOptional = productEntityRepository.findByCodeAndDeletedFalse(request.getProductCode());
		if (!productEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Product not found for Code " + request.getProductCode());
		}

		if (productServiceRepository.countByProductCodeAndServiceName(request.getProductCode(), request.getServiceName()) > 0){
			throw new FraudEngineException("Similar record already exist for code " + request.getProductCode()
					+ " and service name " + request.getServiceName());
		}

		try {
			ProductServiceEntity productService = new ProductServiceEntity();
			productService.setServiceName(request.getServiceName());
			productService.setStatus(Boolean.TRUE);
			productService.setCallbackUrl(request.getCallback());
			productService.setProductCode(request.getProductCode());
			productService.setCreatedBy(request.getCreatedBy());
			productService.setProductEntity(productEntityOptional.get());
			return outputCreatedProductService(productServiceRepository.save(productService));
		} catch (Exception ex) {
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ProductServiceResponse updateProductService(UpdateProductServiceDto request) {
		Optional<ProductEntity> productEntityOptional = productEntityRepository.findByCodeAndDeletedFalse(request.getProductCode());
		if (!productEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Product not found for Code " + request.getProductCode());
		}
		return	productServiceRepository.findById(request.getServiceId()).map(productServiceEntity -> {
				productServiceEntity.setServiceName(request.getServiceName());
				productServiceEntity.setStatus(request.getStatus());
				productServiceEntity.setDescription(request.getDescription());
				productServiceEntity.setUpdatedBy(request.getUpdatedBy());
				productServiceEntity.setCallbackUrl(request.getCallback());
				productServiceEntity.setProductEntity(productEntityOptional.get());
			return outputCreatedProductService(productServiceRepository.save(productServiceEntity));
			}).orElseThrow(()  -> new ResourceNotFoundException("Product service not found for this id " + request.getServiceId()));
	}

	@Transactional
	public Boolean deactivateProductService(Long serviceId){
		Optional<ProductServiceEntity> productServiceEntity = productServiceRepository.findById(serviceId);
		if (!productServiceEntity.isPresent()) {
			throw new ResourceNotFoundException("Product service not found for this id " + serviceId);
		}
		productServiceRepository.deleteById(serviceId);
		return true;
	}
	@Transactional(readOnly = true)
	public Page<ProductServiceEntity> queryAllProductService(String productCode){
		Page<ProductServiceEntity> productServiceEntityPage;
		if (!Objects.isNull(productCode)) {
			productServiceEntityPage = productServiceRepository.findAllByProductCode(productCode, PageRequestUtil.getPageRequest());
		}else{
			productServiceEntityPage = productServiceRepository.findAllByDeletedFalse(PageRequestUtil.getPageRequest());
		}
		return productServiceEntityPage;
	}

	private ProductServiceResponse outputCreatedProductService(ProductServiceEntity productEntityService) {
		ProductServiceResponse productResponse = new ProductServiceResponse();
		BeanUtils.copyProperties(productEntityService, productResponse, "productEntity", "productDataset");
		return productResponse;
	}

	@Transactional
	public void deleteProductInTransaction(ProductEntity productEntity) {
		productServiceRepository.deleteByProductCode(productEntity.getId());
		productEntityRepository.delete(productEntity);
	}
	
}
