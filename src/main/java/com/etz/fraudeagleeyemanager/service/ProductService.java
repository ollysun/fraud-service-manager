package com.etz.fraudeagleeyemanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.entity.Product;
import com.etz.fraudeagleeyemanager.entity.ProductDataset;
import com.etz.fraudeagleeyemanager.repository.ProductDatasetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductDatasetRepository productDatasetRepository;

	public Product createProduct(CreateProductRequest request) {
		Product productEntity = new Product();
		//productEntity.setCode(request.getProductCode());
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
		
		productEntity.setUseCard(true);
		productEntity.setUseAccount(false);
		productEntity.setSupportHold(Status.ENABLED);
		
		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setCreatedBy(request.getCreatedBy());

		// for auditing purpose
		productEntity.setEntityId(null);
		productEntity.setRecordBefore(null);
		productEntity.setRequestDump(request);
		return productRepository.save(productEntity);
	}

	public List<Product> getProduct(String productCode) {
		if (productCode.isEmpty()) {
			return productRepository.findAll();
		}

		List<Product> productList = new ArrayList<>();
		productList.add(productRepository.findById(productCode).get());
		return productList;
	}

	public Product updateProduct(UpdateProductRequest request) {
		Product productEntity = productRepository.findById(request.getProductCode()).get();
		productEntity.setName(request.getProductName());
		productEntity.setDescription(request.getProductDesc());
	//	productEntity.setUseCard(request.getUseCard());
	//	productEntity.setUseAccount(request.getUseAccount());
		productEntity.setCallbackURL(request.getCallback());
		productEntity.setStatus(request.getStatus());
		productEntity.setUpdatedBy(request.getUpdatedBy());
		productEntity.setUpdatedAt(LocalDateTime.now());

		// for auditing purpose
		productEntity.setEntityId(request.getProductCode());
		productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
		productEntity.setRequestDump(request);
		return productRepository.save(productEntity);
	}

	public boolean deleteProduct(String productCode) {
		Product productEntity = productRepository.findById(productCode).get();
		// for auditing purpose
		productEntity.setEntityId(productCode);
		productEntity.setRecordBefore(JsonConverter.objectToJson(productEntity));
		productEntity.setRecordAfter(null);
		productEntity.setRequestDump(productCode);
		
		productRepository.delete(productEntity);
		//productRepository.deleteById(productCode);
		// child records have to be deleted
		return true;
	}

	public ProductDataset addProductDataset(DatasetProductRequest request) {
		ProductDataset prodDatasetEntity = new ProductDataset();
		//prodDatasetEntity.setProductCode(request.getProductCode());
		//prodDatasetEntity.setFieldName(request.getFieldName());
		prodDatasetEntity.setDataType(request.getDataType());
	//	prodDatasetEntity.setMandatory(request.getMandatory());
		//prodDatasetEntity.setAuthorised(request.getAuthorised());
		prodDatasetEntity.setCreatedBy(request.getCreatedBy());
		prodDatasetEntity.setCreatedAt(LocalDateTime.now());
		// productDataset.setUpdatedBy("");
		// productDataset.setUpdatedAt(LocalDateTime.now());

		return productDatasetRepository.save(prodDatasetEntity);
	}

	public List<ProductDataset> getProductDataset(String productCode) {
		if (productCode.isEmpty()) {
			return productDatasetRepository.findAll();
		}

		List<ProductDataset> productDatasetList = new ArrayList<>();
		//productDatasetList.add(productDatasetRepository.findByProductCode(productCode));
		return productDatasetList;
	}

	public ProductDataset updateProductDataset(DatasetProductRequest request) {
		ProductDataset productDatasetEntity = getProductDataset(request.getProductCode()).get(0);
		//productDatasetEntity.setProductCode(request.getProductCode());
		//productDatasetEntity.setFieldName(request.getFieldName());
		productDatasetEntity.setDataType(request.getDataType());
		//productDatasetEntity.setMandatory(request.getMandatory());
		//productDatasetEntity.setAuthorised(request.getAuthorised());
		productDatasetEntity.setUpdatedBy(request.getCreatedBy());
		productDatasetEntity.setUpdatedAt(LocalDateTime.now());

		return productDatasetRepository.save(productDatasetEntity);
	}

	public boolean deleteProductDataset(String productCode) {
		//productDatasetRepository.deleteByProductCode(productCode);
		return true;
	}

}
