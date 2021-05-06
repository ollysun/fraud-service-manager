package com.etz.fraudeagleeyemanager.controller;



import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.entity.Product;
import com.etz.fraudeagleeyemanager.entity.ProductDataset;
import com.etz.fraudeagleeyemanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Product>> createProduct(
			@RequestBody  CreateProductRequest request){
		ModelResponse<Product> response = new ModelResponse<Product>(productService.createProduct(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping(path = "/{product_code}")
	public ModelResponse<Product> updateProduct(@PathVariable(name = "product_code") String productCode,
			@RequestBody @Valid UpdateProductRequest request){
		request.setProductCode(productCode);
		return new ModelResponse<Product>(productService.updateProduct(request));
	}
	
	@DeleteMapping(path = "/{product_code}")
	public BooleanResponse deleteProduct(@PathVariable(name = "product_code") String productCode){
		return new BooleanResponse(productService.deleteProduct(productCode));
	}
	
	@GetMapping
	public CollectionResponse<Product> queryProduct(@RequestParam(name = "productCode", defaultValue = "") String productCode){
		return new CollectionResponse<>(productService.getProduct(productCode.trim()));
	}
	
	@PostMapping(path = "/dataset")
	public ResponseEntity<ModelResponse<ProductDataset>> addProductDataset(@RequestBody @Valid DatasetProductRequest request){
		ModelResponse<ProductDataset> response = new ModelResponse<ProductDataset>(productService.addProductDataset(request));
		response.setStatus(201);
		return new ResponseEntity<ModelResponse<ProductDataset>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/dataset")
	public CollectionResponse<ProductDataset> queryProductDataset(
			@RequestParam(name = "productCode", defaultValue = "") String productCode){
		return new CollectionResponse<>(productService.getProductDataset(productCode.trim()));
	}
	
	@PutMapping(path = "/dataset/{product_code}")
	public ModelResponse<ProductDataset> updateProductDataset(@PathVariable(name = "product_code") String productCode,
                                                              @RequestBody DatasetProductRequest request){
		request.setProductCode(productCode);
		return new ModelResponse<ProductDataset>(productService.updateProductDataset(request));
	}
	
	@DeleteMapping(path = "/dataset/{product_code}")
	public BooleanResponse deleteProductDataset(@PathVariable(name = "product_code") String productCode){
		return new BooleanResponse(productService.deleteProductDataset(productCode));
	}
	
}
