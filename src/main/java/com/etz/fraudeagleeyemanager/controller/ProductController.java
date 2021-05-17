package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.service.ProductService;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<ProductEntity>> createProduct(
			@Valid @RequestBody  CreateProductRequest request){
		ModelResponse<ProductEntity> response = new ModelResponse<ProductEntity>(productService.createProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping
	public ModelResponse<ProductEntity> updateProduct(@PathVariable(name = "product_code") String productCode,
			@RequestBody @Valid UpdateProductRequest request){
		request.setProductCode(productCode);
		return new ModelResponse<>(productService.updateProduct(request));
	}
	
	@DeleteMapping(path = "/{product_code}")
	public BooleanResponse deleteProduct(@PathVariable(name = "product_code") String productCode){
		return new BooleanResponse(productService.deleteProduct(productCode));
	}
	
	@GetMapping
	public CollectionResponse<ProductEntity> queryProduct(@RequestParam(name = "productCode", defaultValue = "")
																	  String productCode){
		return new CollectionResponse<>(productService.getProduct(productCode.trim()));
	}
	
	@PostMapping(path = "/dataset")
	public ResponseEntity<ModelResponse<ProductDataSet>> addProductDataset(@RequestBody @Valid DatasetProductRequest request){
		ModelResponse<ProductDataSet> response = new ModelResponse<>(productService.createProductDataset(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/dataset")
	public CollectionResponse<ProductDataSet> queryProductDataset(
			@RequestParam(name = "productCode", defaultValue = "") String productCode){
		return new CollectionResponse<>(productService.getProductDataset(productCode.trim()));
	}
	
	@PutMapping(path = "/dataset/{product_code}")
	public ModelResponse<ProductDataSet> updateProductDataset(@PathVariable(name = "product_code") String productCode,
                                                              @RequestBody UpdateDataSetRequest request){
		request.setProductCode(productCode);
		return new ModelResponse<>(productService.updateProductDataset(request));
	}
	
	@DeleteMapping(path = "/dataset/{product_code}")
	public BooleanResponse deleteProductDataset(@PathVariable(name = "product_code") String productCode){
		return new BooleanResponse(productService.deleteProductDataset(productCode));
	}
	
}
