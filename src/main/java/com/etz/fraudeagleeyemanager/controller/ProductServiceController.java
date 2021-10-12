package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
import com.etz.fraudeagleeyemanager.dto.request.CreateProductServiceDto;
import com.etz.fraudeagleeyemanager.dto.request.DatasetProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateDataSetRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateProductServiceDto;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductServiceResponse;
import com.etz.fraudeagleeyemanager.dto.response.ServiceDataSetResponse;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.service.ProductService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product/service")
@Validated
public class ProductServiceController {

   private final ProductService productService;

    @GetMapping("/{serviceId}/code/{code}")
    public ResponseEntity<ModelResponse<ProductServiceResponse>> getProductService(@Valid @NotBlank @PathVariable(value = "code") String code,
                                                                                   @Valid @NotBlank @PathVariable(value = "serviceId") String serviceId){
        ModelResponse<ProductServiceResponse> response = new ModelResponse<>(productService.getServiceByCodeAndServiceId(code,serviceId), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @PostMapping
    public ResponseEntity<ModelResponse<ProductServiceResponse>> addProductService(@Valid @RequestBody CreateProductServiceDto request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setCreatedBy(username);
        ModelResponse<ProductServiceResponse> response = new ModelResponse<>(productService.addProductService(request), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @PutMapping
    public ModelResponse<ProductServiceResponse> updateProductService(@RequestBody @Valid UpdateProductServiceDto request,
                                                                      @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setUpdatedBy(username);
        return new ModelResponse<>(productService.updateProductService(request));
    }

    @DeleteMapping("/{serviceId}")
    public BooleanResponse deactivateProductService(@PathVariable @NotBlank String serviceId){
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
        ModelResponse<ServiceDataSet> response = new ModelResponse<>(productService.addServiceDataset(request), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @GetMapping("/dataset")
    public PageResponse<ServiceDataSetResponse> queryServiceDataset(@RequestParam(required = false) String code,
                                                                    @RequestParam(required = false) String serviceId){
        return new PageResponse<>(productService.getServiceDataset(code, serviceId));
    }

    @GetMapping("/dataset/{datasetId}/code/{code}/service/{serviceId}")
    public ResponseEntity<ServiceDataSetResponse> queryServiceDatasetByIds(@PathVariable @NotBlank() String code,
                                                                           @PathVariable @NotBlank String serviceId,
                                                                           @PathVariable @NotNull @Positive Long datasetId){
        return ResponseEntity.ok(productService.getServiceDatasetByIds(datasetId,code, serviceId));
    }

    @PutMapping("/dataset")
    public ResponseEntity<ServiceDataSetResponse> updateServiceDataset(@Valid @RequestBody UpdateDataSetRequest request,
                                                                           @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
        request.setUpdatedBy(username);
        return ResponseEntity.ok(productService.updateServiceDataset(request));
    }

    @DeleteMapping("/dataset/{datasetId}/code/{code}/service/{serviceId}")
    public BooleanResponse deleteServiceDatasetByIds(@PathVariable @NotNull String code,
                                                     @PathVariable @NotNull String serviceId,
                                                     @PathVariable @NotNull Long datasetId){
        return new BooleanResponse(productService.deleteServiceDataset(datasetId,serviceId,code));
    }

}
