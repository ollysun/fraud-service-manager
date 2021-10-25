package com.etz.fraudeagleeyemanager.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.DashboardRequest;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdCustomersResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdOverallTransactionResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdRecentTransaction;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdTransactionPerProduct;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.repository.CardRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.repository.TransactionLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

	private final AccountRepository accountRepository;
	private final CardRepository cardRepository;
	private final ProductEntityRepository productRepository;
	private final TransactionLogRepository transactionLogRepository;

	
	public DashBrdOverallTransactionResponse getOverallTrasaction(DashboardRequest dashboardRequest) {
		return null;
	}
	
	public DashBrdCustomersResponse getCustomer(DashboardRequest dashboardRequest) {
		return null;
	}
	
	public DashBrdTransactionPerProduct getTransactionPerProduct(DashboardRequest dashboardRequest) {
		return null;
	}
	
	public Page<DashBrdRecentTransaction> getRecentTransaction(DashboardRequest dashboardRequest) {
		return null;
	}
}
