package com.etz.fraudeagleeyemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.CreateProductServiceDto;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductServiceDto;
import com.etz.fraudeagleeyemanager.dto.response.ProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductServiceResponse;
import com.etz.fraudeagleeyemanager.dto.response.ServiceDataSetResponse;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductDatasetRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.ProductServiceRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.repository.ProductServiceRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final AppUtil appUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ProductEntityRepository productEntityRepository;
	private final ProductDataSetRepository productDataSetRepository;
	private final ProductRedisRepository productRedisRepository;
	private final ProductDatasetRedisRepository productDatasetRedisRepository;
	private final ProductServiceRepository productServiceRepository;
	private final ProductServiceRedisRepository productServiceRedisRepository;

	@CacheEvict(value = "product", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('PRODUCT.CREATE')")
	public ProductResponse addProduct(CreateProductRequest request) {
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
		return outputProductResponse(addProductEntityToDatabase(productEntity));
	}

	private ProductEntity addProductEntityToDatabase(ProductEntity accountEntity) {
		ProductEntity persistedProductEntity;
		try {
			persistedProductEntity = productEntityRepository.save(accountEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving product entity to database", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addProductEntityToRedis(persistedProductEntity);
		return persistedProductEntity;
	}

	private void addProductEntityToRedis(ProductEntity alreadyPersistedProductEntity) {
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
	@PreAuthorize("hasAuthority('PRODUCT.READ')")
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
	@PreAuthorize("hasAuthority('PRODUCT.UPDATE')")
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
		//	log.error("Error occurred while creating product entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputUpdatedProductResponse(addProductEntityToDatabase(productEntity));
	}

	private ProductResponse outputUpdatedProductResponse(ProductEntity productEntity) {
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(productEntity, productResponse,
				"createdBy, createdAt,productDataset, productRules, products, productLists");
		return productResponse;
	}

	@CacheEvict(value = "product")
	@Transactional(rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('PRODUCT.DELETE')")
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
		//	log.error("Error occurred while deleting product entity from database", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			productRedisRepository.setHashOperations(redisTemplate);
			productRedisRepository.delete(productCode);
		} catch (Exception ex) {
		//	log.error("Error occurred while deleting product entity from Redis", ex);
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
	@PreAuthorize("hasAnyAuthority('SERVICE.DATASET.CREATE','SERVICE.DATASET.APPROVE')")
	public ServiceDataSet addServiceDataset(DatasetProductRequest request) {
		List<ServiceDataSet> serviceDataSetList = new ArrayList<>();

		//check for the code before creating the dataset
		findByCode(request.getProductCode());

		//check for the serviceId
		Optional<ProductServiceEntity> productServiceEntity = productServiceRepository.findById(request.getServiceId());
		if (!productServiceEntity.isPresent()) {
			throw new ResourceNotFoundException("Product service not found for this id " + request.getServiceId());
		}

		//check the fieldname before creating service dataset
		Optional<ServiceDataSet> serviceDataSetOptional = productDataSetRepository.findById(new ProductDatasetId(null, request.getProductCode(), request.getServiceId()));
		serviceDataSetOptional.ifPresent(serviceDataSetList::add);
		for (ServiceDataSet sd: serviceDataSetList){
			if(sd.getFieldName().equalsIgnoreCase(request.getFieldName())){
				throw new FraudEngineException("fieldname : " + sd.getFieldName() + " has already bean created for this service with Id: " + sd.getServiceId());
			}
		}

		ServiceDataSet serviceDataSet = new ServiceDataSet();
		serviceDataSet.setProductCode(request.getProductCode());
		serviceDataSet.setServiceId(request.getServiceId());
		serviceDataSet.setFieldName(request.getFieldName());
		serviceDataSet.setDataType(AppUtil.checkDataType(request.getDataType()));
		serviceDataSet.setMandatory(request.getCompulsory());
		serviceDataSet.setAuthorised(false);
		serviceDataSet.setCreatedBy(request.getCreatedBy());

		// for auditing purpose for CREATE
		serviceDataSet.setEntityId(null);
		serviceDataSet.setRecordBefore(null);
		serviceDataSet.setRequestDump(request);

		return outputCreatedServiceDataset(saveServiceDatasetEntityToDatabase(serviceDataSet, serviceDataSet.getCreatedBy()));
	}

	private ServiceDataSetResponse outputCreatedServiceDataset(ServiceDataSet productEntity) {
		ServiceDataSetResponse productResponse = new ServiceDataSetResponse();
		BeanUtils.copyProperties(productEntity, productResponse, "productEntity");
		return productResponse;
	}

	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('SERVICE.DATASET.READ')")
	public ServiceDataSetResponse getServiceDatasetByIds(Long datasetId, String productCode, String serviceId){
		ServiceDataSet serviceDataSet = productDataSetRepository.findByIds(datasetId,productCode,serviceId)
										.orElseThrow(() -> new ResourceNotFoundException("Service Dataset Details not found"));
		ServiceDataSetResponse productDataSetResponse = new ServiceDataSetResponse();
		BeanUtils.copyProperties(serviceDataSet, productDataSetResponse, "productServiceEntity", "productEntity");
		return productDataSetResponse;
	}

	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	//@PreAuthorize("hasAuthority('SERVICE.DATASET.READ')")
	public Page<ServiceDataSetResponse> getServiceDataset(String productCode, String serviceId) {
		Page<ServiceDataSetResponse> serviceDataSetResponsePage = null;
		List<ServiceDataSet> serviceDataSetList = productDataSetRepository.findAll();

		if (StringUtils.isBlank(serviceId) && StringUtils.isBlank(productCode)){
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(serviceDataSetList), PageRequestUtil.getPageRequest());
		}else if(StringUtils.isNotBlank(productCode) && StringUtils.isBlank(serviceId)){
			List<ServiceDataSet> listProductCode = serviceDataSetList.parallelStream()
											                         .filter(sd -> sd.getProductCode().equalsIgnoreCase(productCode))
													                 .collect(Collectors.toList());
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(listProductCode), PageRequestUtil.getPageRequest());
		}else if (StringUtils.isNotBlank(serviceId) && StringUtils.isBlank(productCode)){
			List<ServiceDataSet> listServiceId = serviceDataSetList.parallelStream()
					.filter(sd -> sd.getServiceId().equalsIgnoreCase(serviceId))
					.collect(Collectors.toList());
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(listServiceId), PageRequestUtil.getPageRequest());
		}else if(StringUtils.isNotBlank(productCode) && StringUtils.isNotBlank(serviceId)){
			List<ServiceDataSet> listServiceIdAndProductCode = serviceDataSetList.parallelStream()
					.filter(sd -> sd.getServiceId().equalsIgnoreCase(serviceId) && sd.getProductCode().equalsIgnoreCase(productCode))
					.collect(Collectors.toList());
			serviceDataSetResponsePage = AppUtil.listConvertToPage(outputServiceDatasetEntity(listServiceIdAndProductCode), PageRequestUtil.getPageRequest());
		}

		return serviceDataSetResponsePage;
	}

	@Transactional(rollbackFor = Throwable.class)
	@PreAuthorize("hasAnyAuthority('SERVICE.DATASET.UPDATE','SERVICE.DATASET.APPROVE')")
	public ServiceDataSetResponse updateServiceDataset(UpdateDataSetRequest request) {

		ServiceDataSet serviceDataSet = productDataSetRepository.findByIds(request.getDatasetId(), request.getProductCode(),
				request.getServiceId())
				.orElseThrow(() -> new ResourceNotFoundException("Service Dataset Details not found"));

		// for auditing purpose for UPDATE
		serviceDataSet.setEntityId(request.getProductCode() + ":" + request.getServiceId() + ":" + request.getDatasetId());
		serviceDataSet.setRecordBefore(JsonConverter.objectToJson(serviceDataSet));
		serviceDataSet.setRequestDump(request);

		serviceDataSet.setDataType(AppUtil.checkDataType(request.getDataType()));
		serviceDataSet.setMandatory(request.getCompulsory());
		serviceDataSet.setAuthorised(false);
		serviceDataSet.setUpdatedBy(request.getUpdatedBy());
		serviceDataSet.setFieldName(request.getFieldName());

		ServiceDataSetResponse productDataSetResponse = new ServiceDataSetResponse();
		BeanUtils.copyProperties(serviceDataSet, productDataSetResponse, "createdBy", "createdAt","productServiceEntity", "productEntity");
		saveServiceDatasetEntityToDatabase(serviceDataSet, serviceDataSet.getUpdatedBy());
		return productDataSetResponse;
	}

	@SuppressWarnings("unused")
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
	@PreAuthorize("hasAnyAuthority('SERVICE.DATASET.DELETE','SERVICE.DATASET.APPROVE')")
	public boolean deleteServiceDataset(Long datasetId, String serviceId, String code) {
		ServiceDataSet serviceDataSet = productDataSetRepository.findByIds(datasetId, code, serviceId)
				.orElseThrow(() -> new ResourceNotFoundException("Service Dataset Details not found"));

			productDatasetRedisRepository.setHashOperations(redisTemplate);
		
			// for auditing purpose for DELETE
			serviceDataSet.setEntityId(serviceId);
			serviceDataSet.setRecordBefore(JsonConverter.objectToJson(serviceDataSet));
			serviceDataSet.setRecordAfter(null);
			serviceDataSet.setRequestDump(serviceId);

			try {
				productDataSetRepository.delete(serviceDataSet);
			} catch (Exception ex) {
			//	log.error("Error occurred while deleting product dataset entity from database", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
			}
			try {
				String redisId = code + ":" + datasetId + ":" + serviceId;
				productDatasetRedisRepository.delete(redisId);
			} catch (Exception ex) {
			//	log.error("Error occurred while deleting product dataset entity from Redis", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
			}
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


	private ServiceDataSet saveServiceDatasetEntityToDatabase(ServiceDataSet serviceDatasetEntity, String createdBy) {
		ServiceDataSet persistedServiceDatasetEntity = new ServiceDataSet();
		try {
			persistedServiceDatasetEntity = productDataSetRepository.save(serviceDatasetEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving ServiceDataset entity to database", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addServiceDatasetEntityToRedis(persistedServiceDatasetEntity);
		// create & user notification
		appUtil.createUserNotification(AppConstant.SERVICE_DATASET, persistedServiceDatasetEntity.getProductCode()+"_"+persistedServiceDatasetEntity.getServiceId()+"_"+persistedServiceDatasetEntity.getDatasetId(), createdBy);
		return persistedServiceDatasetEntity;
	}

	private void addServiceDatasetEntityToRedis(ServiceDataSet alreadyPersistedServiceDatasetEntity) {
		try {
			productDatasetRedisRepository.setHashOperations(redisTemplate);
			productDatasetRedisRepository.update(alreadyPersistedServiceDatasetEntity);
		} catch (Exception ex) {
			log.error("Error occurred while saving ServiceDataset to Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@CacheEvict(value = "Service", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('PRODUCT.SERVICE.CREATE')")
	public ProductServiceResponse addProductService(CreateProductServiceDto request) {
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

			// for auditing purpose for CREATE
			productService.setEntityId(null);
			productService.setRecordBefore(null);
			productService.setRequestDump(request);
			
			return outputCreatedProductService(productServiceRepository.save(productService));
		} catch (Exception ex) {
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
	}
	
	@Transactional(rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('PRODUCT.SERVICE.UPDATE')")
	public ProductServiceResponse updateProductService(UpdateProductServiceDto request) {
		Optional<ProductEntity> productEntityOptional = productEntityRepository.findByCodeAndDeletedFalse(request.getProductCode());
		if (!productEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Product not found for Code " + request.getProductCode());
		}
		return	productServiceRepository.findById(request.getServiceId()).map(productServiceEntity -> {
			// for auditing purpose for UPDATE
			productServiceEntity.setEntityId(request.getServiceId());
			productServiceEntity.setRecordBefore(JsonConverter.objectToJson(productServiceEntity));
			productServiceEntity.setRequestDump(request);

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
	@PreAuthorize("hasAuthority('PRODUCT.SERVICE.DELETE')")
	public Boolean deactivateProductService(String serviceId){
		Optional<ProductServiceEntity> productServiceEntityOptional = productServiceRepository.findById(serviceId);
		if (!productServiceEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Product service not found for this id " + serviceId);
		}

		ProductServiceEntity productServiceEntity = productServiceEntityOptional.get();
		// for auditing purpose for DELETE
		productServiceEntity.setEntityId(serviceId);
		productServiceEntity.setRecordBefore(JsonConverter.objectToJson(productServiceEntity));
		productServiceEntity.setRecordAfter(null);
		productServiceEntity.setRequestDump(serviceId);
		
		try {
			productServiceRepository.delete(productServiceEntity);
			//productServiceRepository.deleteById(serviceId)
		} catch (Exception ex) {
			log.error("Error occurred while deleting product service entity from database", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		return true;
	}

	@Transactional(readOnly = true)
	@PreAuthorize("hasAuthority('PRODUCT.SERVICE.READ')")
	public Page<ProductServiceEntity> queryAllProductService(String productCode){
		Page<ProductServiceEntity> productServiceEntityPage;
		List<ProductServiceEntity> productServiceEntityList = productServiceRepository.findAll();
		if (StringUtils.isNotBlank(productCode)) {
			List<ProductServiceEntity> listProductCode = productServiceEntityList.stream()
					.filter(sd -> sd.getProductCode().equalsIgnoreCase(productCode))
					.collect(Collectors.toList());
			productServiceEntityPage = AppUtil.listConvertToPage(listProductCode,PageRequestUtil.getPageRequest());
		}else{
			productServiceEntityPage = AppUtil.listConvertToPage(productServiceEntityList,PageRequestUtil.getPageRequest());
		}
		return productServiceEntityPage;
	}

	@Transactional(readOnly = true)
	//@PreAuthorize("hasAuthority('PRODUCT.SERVICE.READ')")
	public ProductServiceResponse getServiceByCodeAndServiceId(String code, String serviceId){
		ProductServiceEntity productServiceEntity = productServiceRepository.findByServiceIdAndProductCode(serviceId, code)
													.orElseThrow(() -> new ResourceNotFoundException(" code and serviceId not found"));
		return outputCreatedProductService(productServiceEntity);
	}

	private ProductServiceResponse outputCreatedProductService(ProductServiceEntity productEntityService) {
		addProductServiceEntityToRedis(productEntityService);
		ProductServiceResponse productResponse = new ProductServiceResponse();
		BeanUtils.copyProperties(productEntityService, productResponse, "productEntity", "productDataset");
		return productResponse;
	}

	private void addProductServiceEntityToRedis(ProductServiceEntity alreadyPersistedProductServiceEntity) {
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
