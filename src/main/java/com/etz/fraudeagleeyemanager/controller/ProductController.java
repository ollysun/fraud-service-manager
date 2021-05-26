package com.etz.fraudeagleeyemanager.controller;



import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<ProductEntity>> createProduct(
			@Valid @RequestBody  CreateProductRequest request){
		ModelResponse<ProductEntity> response = new ModelResponse<>(productService.createProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping
	public ModelResponse<ProductEntity> updateProduct(@RequestBody @Valid UpdateProductRequest request){
		return new ModelResponse<>(productService.updateProduct(request));
	}
	
	@DeleteMapping(path = "/{code}")
	public BooleanResponse deleteProduct(@PathVariable(name = "code") String code){
		return new BooleanResponse(productService.deleteProduct(code));
	}
	
	@GetMapping
	public ResponseEntity<CollectionResponse<ProductResponse>> queryProduct(@RequestParam(name = "code", required = false)
																	  String code){
		List<ProductResponse> userResponseList = productService.getProduct(code);
		CollectionResponse<ProductResponse> collectionResponse = new CollectionResponse<>(userResponseList);
		collectionResponse.setMessage("All Product");
		return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
	}
	
	@PostMapping(path = "/dataset")
	public ResponseEntity<ModelResponse<ProductDataSet>> addProductDataset(@RequestBody @Valid DatasetProductRequest request){
		ModelResponse<ProductDataSet> response = new ModelResponse<>(productService.createProductDataset(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/dataset")
	public ResponseEntity<CollectionResponse<ProductDataSetResponse>> queryProductDataset(
			@RequestParam(name = "code", required = false) String code){

		List<ProductDataSetResponse> userResponseList = productService.getProductDataset(code);
		CollectionResponse<ProductDataSetResponse> collectionResponse = new CollectionResponse<>(userResponseList);
		collectionResponse.setMessage("All Dataset");
		return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
	}
	
	@PutMapping(path = "/dataset")
	public ModelResponse<ProductDataSet> updateProductDataset(@RequestBody UpdateDataSetRequest request){
		return new ModelResponse<>(productService.updateProductDataset(request));
	}
	
	@DeleteMapping(path = "/dataset/{code}")
	public BooleanResponse deleteProductDataset(@PathVariable(name = "code") String code){
		return new BooleanResponse(productService.deleteProductDataset(code));
	}
	
}
