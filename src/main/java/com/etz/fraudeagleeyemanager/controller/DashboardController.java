package com.etz.fraudeagleeyemanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.DashboardRequest;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdCustomersResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdOverallTransactionResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdRecentTransaction;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdTransactionPerProduct;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;
	
	@GetMapping("/transaction")
	public ModelResponse<DashBrdOverallTransactionResponse> queryOverallTransaction(@RequestParam(required = false) String start, @RequestParam(required = false) String end, 
			@RequestParam(required = false) String product) {
		return new ModelResponse<>(dashboardService.getOverallTrasaction(new DashboardRequest(start, end, product)));
	}
	
	@GetMapping("/customer")
	public ModelResponse<DashBrdCustomersResponse> queryCustomer(@RequestParam(required = false) String start, @RequestParam(required = false) String end, 
			@RequestParam(required = false) String product) {
		return new ModelResponse<>(dashboardService.getCustomer(new DashboardRequest(start, end, product)));
	}
	
	@GetMapping("/transaction/product")
	public CollectionResponse<DashBrdTransactionPerProduct> queryTransactionPerProduct(@RequestParam(required = false) String start, @RequestParam(required = false) String end, 
			@RequestParam(required = false) String product) {
		return new CollectionResponse<>(dashboardService.getTransactionPerProduct(new DashboardRequest(start, end, product)));
	}
	
	@GetMapping("/transaction/recent")
	public CollectionResponse<DashBrdRecentTransaction> queryRecentTransaction(@RequestParam(required = false) String start, @RequestParam(required = false) String end, 
			@RequestParam(required = false) String product, @RequestParam(required = true) Long limit) {
		return new CollectionResponse<>(dashboardService.getRecentTransaction(new DashboardRequest(start, end, product), limit));
	}
}
