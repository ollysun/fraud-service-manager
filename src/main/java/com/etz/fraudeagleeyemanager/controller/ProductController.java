package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductResponse;
import com.etz.fraudeagleeyemanager.service.ProductService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
@Validated
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ModelResponse<ProductResponse>> addProduct(@Valid @RequestBody  CreateProductRequest request,
																		@ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<ProductResponse> response = new ModelResponse<>(productService.addProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping
	public ModelResponse<ProductResponse> updateProduct(@RequestBody @Valid UpdateProductRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(productService.updateProduct(request));
	}
	
	@DeleteMapping("/{code}")
	public BooleanResponse deleteProduct(@PathVariable @NotNull String code){
		return new BooleanResponse(productService.deleteProduct(code));
	}
	
	@GetMapping
	public CollectionResponse<ProductResponse> queryProduct(@RequestParam(required = false) String code){
		return new CollectionResponse<>(productService.getProduct(code), "All Product");
	}
	


	
}
