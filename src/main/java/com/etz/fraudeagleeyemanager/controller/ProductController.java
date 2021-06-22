package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductDataSetResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductResponse;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ModelResponse<ProductResponse>> createProduct(@Valid @RequestBody  CreateProductRequest request){
		ModelResponse<ProductResponse> response = new ModelResponse<>(productService.createProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping
	public ModelResponse<ProductResponse> updateProduct(@RequestBody @Valid UpdateProductRequest request){
		return new ModelResponse<>(productService.updateProduct(request));
	}
	
	@DeleteMapping("/{code}")
	public BooleanResponse deleteProduct(@PathVariable String code){
		return new BooleanResponse(productService.deleteProduct(code));
	}
	
	@GetMapping
	public CollectionResponse<ProductResponse> queryProduct(String code){
		return new CollectionResponse<>(productService.getProduct(code), "All Product");
	}
	
	@PostMapping("/dataset")
	public ResponseEntity<ModelResponse<ProductDataSet>> addProductDataset(@RequestBody @Valid DatasetProductRequest request){
		ModelResponse<ProductDataSet> response = new ModelResponse<>(productService.createProductDataset(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@GetMapping("/dataset")
	public CollectionResponse<ProductDataSetResponse> queryProductDataset(String code){
		return new CollectionResponse<>(productService.getProductDataset(code), "All Dataset");
	}
	
	@PutMapping("/dataset")
	public CollectionResponse<ProductDataSetResponse> updateProductDataset(@RequestBody UpdateDataSetRequest request){
		return new CollectionResponse<>(productService.updateProductDataset(request));
	}
	
	@DeleteMapping("/dataset/{code}")
	public BooleanResponse deleteProductDataset(@PathVariable String code){
		return new BooleanResponse(productService.deleteProductDataset(code));
	}
	
}
