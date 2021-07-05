package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.*;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product/service")
public class ProductServiceController {

   private final ProductService productService;

    @GetMapping("/{serviceId}/code/{code}")
    public ResponseEntity<ModelResponse<ProductServiceResponse>> getProductService(@Valid @NotBlank @PathVariable(value = "code") String code,
                                                                                   @Valid @NotBlank @PathVariable(value = "serviceId") String serviceId){
        ModelResponse<ProductServiceResponse> response = new ModelResponse<>(productService.getServiceByCodeAndServiceId(code,serviceId), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @PostMapping
    public ResponseEntity<ModelResponse<ProductServiceResponse>> createProductService(@Valid @RequestBody CreateProductServiceDto request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setCreatedBy(username);
        ModelResponse<ProductServiceResponse> response = new ModelResponse<>(productService.createProductService(request), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @PutMapping
    public ModelResponse<ProductServiceResponse> updateProductService(@RequestBody @Valid UpdateProductServiceDto request,
                                                                      @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setUpdatedBy(username);
        return new ModelResponse<>(productService.updateProductService(request));
    }

    @DeleteMapping("/{serviceId}")
    public BooleanResponse deactivateProductService(@PathVariable String serviceId){
        return new BooleanResponse(productService.deactivateProductService(serviceId));
    }

    @GetMapping
    public PageResponse<ProductServiceEntity> queryProductService(@RequestParam(required = false) String code){
        return new PageResponse<>(productService.queryAllProductService(code));
    }

    @PostMapping("/dataset")
    public ResponseEntity<ModelResponse<ServiceDataSet>> addServiceDataset(@RequestBody @Valid DatasetProductRequest request,
                                                                           @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setCreatedBy(username);
        ModelResponse<ServiceDataSet> response = new ModelResponse<>(productService.createServiceDataset(request), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @GetMapping("/dataset")
    public PageResponse<ServiceDataSetResponse> queryServiceDataset(@RequestParam(required = false) String code,
                                                                    @RequestParam(required = false) String serviceId){
        return new PageResponse<>(productService.getServiceDataset(code, serviceId));
    }

    @PutMapping("/dataset")
    public CollectionResponse<ServiceDataSetResponse> updateServiceDataset(@RequestBody UpdateDataSetRequest request,
                                                                           @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setUpdatedBy(username);
        return new CollectionResponse<>(productService.updateServiceDataset(request));
    }

    @DeleteMapping("/dataset/{serviceId}")
    public BooleanResponse deleteServiceDataset(@PathVariable String serviceId){
        return new BooleanResponse(productService.deleteServiceDataset(serviceId));
    }

}
