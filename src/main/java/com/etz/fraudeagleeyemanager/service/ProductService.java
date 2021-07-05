package com.etz.fraudeagleeyemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.etz.fraudeagleeyemanager.dto.request.*;
import com.etz.fraudeagleeyemanager.dto.response.ProductServiceResponse;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.redisrepository.ProductServiceRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ProductServiceRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.response.ServiceDataSetResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductResponse;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
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
	private final ProductServiceRedisRepository productServiceRedisRepository;

	@CacheEvict(value = "product", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
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

	@Cacheable(value="product")
	@Transactional(readOnly = true)
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

	@CacheEvict(value = "product", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
	public ProductResponse updateProduct(UpdateProductRequest request) {
		ProductEntity productEntity = findByCode(request.getProductCode()).get();

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

	@CacheEvict(value = "product")
	@Transactional(rollbackFor = Throwable.class)
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

	@Transactional(rollbackFor = Throwable.class)
	public ServiceDataSet createServiceDataset(DatasetProductRequest request) {
		//check for the code before creating the dataset
		findByCode(request.getProductCode());

		ServiceDataSet prodDatasetEntity = new ServiceDataSet();
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
		return outputCreatedServiceDataset(saveServiceDatasetEntityToDatabase(prodDatasetEntity));
	}

	private ServiceDataSetResponse outputCreatedServiceDataset(ServiceDataSet productEntity) {
		ServiceDataSetResponse productResponse = new ServiceDataSetResponse();
		BeanUtils.copyProperties(productEntity, productResponse, "productEntity");
		return productResponse;
	}

	@Transactional(readOnly = true)
	public Page<ServiceDataSetResponse> getServiceDataset(String productCode, String serviceId, Long dataSetId) {
		Page<ServiceDataSetResponse> serviceDataSetResponsePage;
		List<ServiceDataSet> serviceDataSetList = new ArrayList<>();
		if (StringUtils.isNotBlank(productCode)) {
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(productDataSetRepository.findByProductCode(productCode)), PageRequestUtil.getPageRequest());
		}else if (StringUtils.isBlank(serviceId) && StringUtils.isBlank(dataSetId.toString())){
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(productDataSetRepository.findAll()), PageRequestUtil.getPageRequest());
		}else{
			Optional<ServiceDataSet> serviceDataSetOptional = productDataSetRepository.findById(new ProductDatasetId(dataSetId, productCode, serviceId));
			serviceDataSetOptional.ifPresent(serviceDataSetList::add);
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(serviceDataSetList), PageRequestUtil.getPageRequest());
		}
		return serviceDataSetResponsePage;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ServiceDataSetResponse> updateServiceDataset(UpdateDataSetRequest request) {
		List<ServiceDataSet> serviceDataSetList = productDataSetRepository.findByServiceId(request.getServiceId());
		if (serviceDataSetList.isEmpty()) {
			throw new ResourceNotFoundException("Service dataset details not found for this ServiceId " + request.getServiceId());
		}

		List<ServiceDataSet> updatedDatasetList = new ArrayList<>();
		serviceDataSetList.forEach(productDataSet -> {
			try {
				// for auditing purpose for UPDATE
				productDataSet.setEntityId(request.getServiceId());
				productDataSet.setRecordBefore(JsonConverter.objectToJson(productDataSet));
				productDataSet.setRequestDump(request);

				productDataSet.setDataType(request.getDataType());
				productDataSet.setMandatory(request.getCompulsory());
				productDataSet.setAuthorised(request.getAuthorised());
				productDataSet.setUpdatedBy(request.getUpdatedBy());
				productDataSet.setFieldName(request.getFieldName());
			} catch (Exception ex) {
				log.error("Error occurred while creating product dataset entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			updatedDatasetList.add(saveServiceDatasetEntityToDatabase(productDataSet));
		});
		return outputUpdatedProductDatasetResponse(updatedDatasetList);
	}

	private List<ServiceDataSetResponse> outputUpdatedProductDatasetResponse(List<ServiceDataSet> serviceDataSetList) {
		List<ServiceDataSetResponse> updatedProductDatasetResponseList = new ArrayList<>();
		serviceDataSetList.forEach(productDataSet -> {
			ServiceDataSetResponse productDataSetResponse = new ServiceDataSetResponse();
			BeanUtils.copyProperties(productDataSet, productDataSetResponse, "createdBy", "createdAt", "productEntity");
			updatedProductDatasetResponseList.add(productDataSetResponse);
		});
		return updatedProductDatasetResponseList;
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteServiceDataset(String serviceId) {
		List<ServiceDataSet> serviceDatasetEntityList = productDataSetRepository.findByServiceId(serviceId);
		if (serviceDatasetEntityList.isEmpty()) {
			throw new ResourceNotFoundException("Service dataset details not found for this serviceId " + serviceId);
		}
		productDatasetRedisRepository.setHashOperations(redisTemplate);
		serviceDatasetEntityList.forEach(productDataset -> {
			// for auditing purpose for DELETE
			productDataset.setEntityId(serviceId);
			productDataset.setRecordBefore(JsonConverter.objectToJson(productDataset));
			productDataset.setRecordAfter(null);
			productDataset.setRequestDump(serviceId);

			try {
				productDataSetRepository.delete(serviceId);
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

	private List<ServiceDataSetResponse> outputServiceDatasetEntity(List<ServiceDataSet> serviceDatasetList) {
		List<ServiceDataSetResponse> productResponseList = new ArrayList<>();
		serviceDatasetList.forEach(productVal -> {
			ServiceDataSetResponse productResponse = new ServiceDataSetResponse();
			BeanUtils.copyProperties(productVal, productResponse, "productEntity");
			productResponseList.add(productResponse);
		});
		return productResponseList;
	}


	private ServiceDataSet saveServiceDatasetEntityToDatabase(ServiceDataSet accountEntity) {
		ServiceDataSet persistedServiceDatasetEntity;
		try {
			persistedServiceDatasetEntity = productDataSetRepository.save(accountEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving product entity to database", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveServiceDatasetEntityToRedis(persistedServiceDatasetEntity);
		return persistedServiceDatasetEntity;
	}

	private void saveServiceDatasetEntityToRedis(ServiceDataSet alreadyPersistedServiceDatasetEntity) {
		try {
			productDatasetRedisRepository.setHashOperations(redisTemplate);
			productDatasetRedisRepository.update(alreadyPersistedServiceDatasetEntity);
		} catch (Exception ex) {
			// TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving product entity to Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@CacheEvict(value = "Service", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
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
			productService.setServiceId(request.getServiceId());
			productService.setServiceName(request.getServiceName());
			productService.setStatus(Boolean.TRUE);
			productService.setCallbackUrl(request.getCallback());
			productService.setProductCode(request.getProductCode());
			productService.setCreatedBy(request.getCreatedBy());
			productService.setDescription(request.getDescription());
			productService.setProductEntity(productEntityOptional.get());
			return outputCreatedProductService(productServiceRepository.save(productService));
		} catch (Exception ex) {
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	@Transactional(rollbackFor = Throwable.class)
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

	@Transactional(rollbackFor = Throwable.class)
	public Boolean deactivateProductService(String serviceId){
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
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productServiceEntityPage = productServiceRepository.findAllByProductCode(productCode, PageRequestUtil.getPageRequest());
		}else{
			productServiceEntityPage = productServiceRepository.findAllByDeletedFalse(PageRequestUtil.getPageRequest());
		}
		return productServiceEntityPage;
	}

	@Transactional(readOnly = true)
	public ProductServiceResponse getServiceByCodeAndServiceId(String code, String serviceId){
		ProductServiceEntity productServiceEntity = productServiceRepository.findByServiceIdAndProductCode(serviceId, code)
													.orElseThrow(() -> new ResourceNotFoundException(" code and serviceId not found"));
		return outputCreatedProductService(productServiceEntity);
	}

	private ProductServiceResponse outputCreatedProductService(ProductServiceEntity productEntityService) {
		saveProductServiceEntityToRedis(productEntityService);
		ProductServiceResponse productResponse = new ProductServiceResponse();
		BeanUtils.copyProperties(productEntityService, productResponse, "productEntity", "productDataset");
		return productResponse;
	}

	private void saveProductServiceEntityToRedis(ProductServiceEntity alreadyPersistedProductServiceEntity) {
		try {
			productServiceRedisRepository.setHashOperations(redisTemplate);
			productServiceRedisRepository.update(alreadyPersistedProductServiceEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving product entity to Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteProductInTransaction(ProductEntity productEntity) {
		productServiceRepository.deleteByProductCode(productEntity.getId());
		productEntityRepository.delete(productEntity);
	}
	
}
