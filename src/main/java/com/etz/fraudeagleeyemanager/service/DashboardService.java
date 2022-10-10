package com.etz.fraudeagleeyemanager.service;

import static com.etz.fraudeagleeyemanager.constant.AppConstant.DASHBOARD_REQ_DATE_FORMAT;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.DashboardRequest;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdCustomersResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdOverallTransactionResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdRecentTransaction;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdTransactionPerProdResponse;
import com.etz.fraudeagleeyemanager.dto.response.DashBrdTransactionPerProduct;
import com.etz.fraudeagleeyemanager.dto.response.TotalToday;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.TransactionLogEntity;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.repository.CardProductRepository;
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
	private final AccountProductRepository accountProductRepository;
	private final CardRepository cardRepository;
	private final CardProductRepository cardProductRepository;
	private final ProductEntityRepository productRepository;
	private final TransactionLogRepository transactionLogRepository;

	
	public DashBrdOverallTransactionResponse getOverallTrasaction(DashboardRequest dashboardRequest) {
		log.info("Get Overall Transaction Dasboard request object - {}", dashboardRequest);
		
		List<TransactionLogEntity> allTrasactions;
		if(Objects.nonNull(dashboardRequest.getProductCode()) && productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			allTrasactions = transactionLogRepository.findByProductCode(dashboardRequest.getProductCode());
			log.info("Number of transactions filtered by productCode: {}", allTrasactions.size());
		
		} else if(Objects.nonNull(dashboardRequest.getProductCode()) && !productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			log.error("ProductCode {} does not exist", dashboardRequest.getProductCode());
			throw new FraudEngineException("Product with Code " + dashboardRequest.getProductCode() + " does not exist");
		
		} else {
			allTrasactions = transactionLogRepository.findAll();
			log.info("Number of all transactions: {}", allTrasactions.size());
		}
		
		BigDecimal totalAmount = allTrasactions.stream().map(TransactionLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.HALF_UP);
		BigDecimal totalAmountToday = allTrasactions.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).map(TransactionLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.HALF_UP);
		//Long totalCount = allTrasactions.stream().count();
		//Long totalCountToday = allTrasactions.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).count();
		long totalCount;
		long totalCountToday = 0;

		LocalDate startDate;
		LocalDate endDate;
		try {
			if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(dashboardRequest.getEndDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				if (startDate.isAfter(endDate)) {
					log.error("Start date, {}, cannot be after the end date {}", dashboardRequest.getStartDate(), dashboardRequest.getEndDate());
					throw new FraudEngineException("Start date cannot be after end date");
				}

				totalAmount = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.map(TransactionLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
						.setScale(0, RoundingMode.HALF_UP);

				totalCount = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.count();

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Get Overall Transaction Stats After Date filter");
				//log.info("totalAmount: {}, totalAmountToday: {}, totalCount: {}, totalCountToday: {}", totalAmount, totalAmountToday, totalCount, totalCountToday);

			} else if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.isNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT).format(LocalDate.now()), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				if (startDate.isAfter(endDate)) {
					log.error("Start date, {}, cannot be after the end date {}", dashboardRequest.getStartDate(), dashboardRequest.getEndDate());
					throw new FraudEngineException("Start date cannot be after end date");
				}

				totalAmount = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.map(TransactionLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
						.setScale(0, RoundingMode.HALF_UP);

				totalCount = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.count();

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Get Overall Transaction Stats After Date filter");
				log.info("totalAmount: {}, totalAmountToday: {}, totalCount: {}, totalCountToday: {}", totalAmount, totalAmountToday, totalCount, totalCountToday);

			} else if (Objects.isNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				log.error("Start date must be specified alongside the end date {}", dashboardRequest.getEndDate());
				throw new FraudEngineException("Start date must be specified alongside the end date");
			}else{
				//Long totalCount = allTrasactions.stream().count();
				//Long totalCountToday = allTrasactions.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).count();
				totalCount = allTrasactions.stream().count();
				totalCountToday = allTrasactions.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).count();
				log.info("Get Overall Transaction Stats Before Date filter");
				log.info("totalAmount: {}, totalAmountToday: {}, totalCount: {}, totalCountToday: {}", totalAmount, totalAmountToday, totalCount, totalCountToday);

			}
		} catch (DateTimeParseException dtpEx) {
			log.error("Wrong date format was received as input: {}", dtpEx.getLocalizedMessage());
			throw new FraudEngineException("Start and End dates should be in the format " + DASHBOARD_REQ_DATE_FORMAT);
		}
		
		DashBrdOverallTransactionResponse response = new DashBrdOverallTransactionResponse();
		response.setTransactionValue(new TotalToday(totalAmount.longValue(), totalAmountToday.longValue()));
		response.setTransactionCount(new TotalToday(totalCount, totalCountToday));

		log.info("DashBrdOverallTransactionResponse: {}", response);
		return response;
	}
	
	public DashBrdCustomersResponse getCustomer(DashboardRequest dashboardRequest) {
		log.info("Get Customer Dashboard request object - {}", dashboardRequest);

		List<Card> allCards;
		List<Account> allAccounts;
		if(Objects.nonNull(dashboardRequest.getProductCode()) && productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			// get all card Ids pertaining to product code
			List<Long> cardProductIds = cardProductRepository.findByProductCode(dashboardRequest.getProductCode()).stream().map(CardProduct::getCardId).collect(Collectors.toList());
			// filter cards using the card Ids from cardProduct
			allCards = cardRepository.findAll().stream().filter(card -> cardProductIds.contains(card.getId())).collect(Collectors.toList());
			
			// get all account Ids assigned to product code
			List<Long> accountProductIds = accountProductRepository.findByProductCode(dashboardRequest.getProductCode()).stream().map(AccountProduct::getAccountId).collect(Collectors.toList());
			// filter accounts using account Ids from accountProduct
			allAccounts = accountRepository.findAll().stream().filter(account -> accountProductIds.contains(account.getId())).collect(Collectors.toList());
			
			log.info("Number of Cards filtered by productCode: {}", allCards.size());
			log.info("Number of Accounts filtered by productCode: {}", allAccounts.size());
			
		} else if(Objects.nonNull(dashboardRequest.getProductCode()) && !productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			log.error("ProductCode {} does not exist", dashboardRequest.getProductCode());
			throw new FraudEngineException("Product with Code " + dashboardRequest.getProductCode() + " does not exist");
		} else {
			allCards = cardRepository.findAll();
			allAccounts = accountRepository.findAll();
			log.info("Number of all Cards: {}", allCards.size());
			log.info("Number of all Accounts: {}", allAccounts.size());
		}
		
		Long totalCard = allCards.stream().distinct().count();
		Long totalCardToday = allCards.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).distinct().count();
		Long totalAccount = allAccounts.stream().distinct().count();
		Long totalAccountToday = allAccounts.stream().filter(t -> t.getCreatedAt().toLocalDate().isEqual(LocalDate.now())).distinct().count();
		
		log.info("Get Customer Stats Before Date filter");
		log.info("totalCard: {}, totalCardToday: {}, totalAccount: {}, totalAccountToday: {}", totalCard, totalCardToday, totalAccount, totalAccountToday);
		LocalDate startDate;
		LocalDate endDate;
		try {
			if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(dashboardRequest.getEndDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				if (startDate.isAfter(endDate)) {
					log.error("Start date, {}, cannot be after the end date {}", dashboardRequest.getStartDate(), dashboardRequest.getEndDate());
					throw new FraudEngineException("Start date cannot be after end date");
				}

				totalCard = allCards.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.distinct().count();

				totalAccount = allCards.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.distinct().count();

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Get Customer Stats After Date filter");
				log.info("totalCard: {}, totalCardToday: {}, totalAccount: {}, totalAccountToday: {}", totalCard, totalCardToday, totalAccount, totalAccountToday);

			} else if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.isNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT).format(LocalDate.now()), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				totalCard = allCards.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.distinct().count();

				totalAccount = allCards.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.distinct().count();

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Get Customer Stats After Date filter");
				log.info("totalCard: {}, totalCardToday: {}, totalAccount: {}, totalAccountToday: {}", totalCard, totalCardToday, totalAccount, totalAccountToday);

			} else if (Objects.isNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				log.error("Start date must be specified alongside the end date {}", dashboardRequest.getEndDate());
				throw new FraudEngineException("Start date must be specified alongside the end date");
			}
		} catch (DateTimeParseException dtpEx) {
			log.error("Wrong date format was received as input: {}", dtpEx.getLocalizedMessage());
			throw new FraudEngineException("Start and End dates should be in the format " + DASHBOARD_REQ_DATE_FORMAT);
		}
		
		DashBrdCustomersResponse response = new DashBrdCustomersResponse();
		response.setCard(new TotalToday(totalCard, totalCardToday));
		response.setAccount(new TotalToday(totalAccount, totalAccountToday));
		
		log.info("DashBrdCustomersResponse: {}", response);
		return response;
	}
	
	public DashBrdTransactionPerProdResponse getTransactionPerProduct(DashboardRequest dashboardRequest) {
		log.info("Get Transaction Per Product Dashboard request object - {}", dashboardRequest);
		
		List<TransactionLogEntity> allTrasactions = transactionLogRepository.findAll();
		long totalFlaggedTransaction = allTrasactions.stream().filter(TransactionLogEntity::getIsFraud).count();
		
		Map<String, List<TransactionLogEntity>> groupedTransactionsByProductCode = allTrasactions.stream().collect(Collectors.groupingBy(TransactionLogEntity::getProductCode));

		log.info("Number of GroupedTransactionsByProductCode before date filter: {}", groupedTransactionsByProductCode.size());
		
		LocalDate startDate;
		LocalDate endDate;
		try {
			if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(dashboardRequest.getEndDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				if (startDate.isAfter(endDate)) {
					log.error("Start date, {}, cannot be after the end date {}", dashboardRequest.getStartDate(), dashboardRequest.getEndDate());
					throw new FraudEngineException("Start date cannot be after end date");
				}

				groupedTransactionsByProductCode = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.collect(Collectors.groupingBy(TransactionLogEntity::getProductCode));

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Number of GroupedTransactionsByProductCode after date filter: {}", groupedTransactionsByProductCode.size());

			} else if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.isNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT).format(LocalDate.now()), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				groupedTransactionsByProductCode = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.collect(Collectors.groupingBy(TransactionLogEntity::getProductCode));

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Number of GroupedTransactionsByProductCode after date filter: {}", groupedTransactionsByProductCode.size());
				
			} else if (Objects.isNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				log.error("Start date must be specified alongside the end date {}", dashboardRequest.getEndDate());
				throw new FraudEngineException("Start date must be specified alongside the end date");
			}
		} catch (DateTimeParseException dtpEx) {
			log.error("Wrong date format was received as input: {}", dtpEx.getLocalizedMessage());
			throw new FraudEngineException("Start and End dates should be in the format " + DASHBOARD_REQ_DATE_FORMAT);
		}

		List<DashBrdTransactionPerProduct> transactionPerProducts = new ArrayList<>();
		for(Map.Entry<String, List<TransactionLogEntity>> entry : groupedTransactionsByProductCode.entrySet()) {
			log.info("[{}] Number of transactions for productCode {}", entry.getValue().stream().count(), entry.getKey());
			
			DashBrdTransactionPerProduct transactionPerProd = new DashBrdTransactionPerProduct();
			transactionPerProd.setProductCode(entry.getKey());
			if (productRepository.findById(entry.getKey()).map(ProductEntity::getName).isPresent()) {
				transactionPerProd.setName(productRepository.findById(entry.getKey()).map(ProductEntity::getName).get());
			}
			transactionPerProd.setTotalTransaction(entry.getValue().stream().map(TransactionLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.HALF_UP));
			transactionPerProd.setTransactionCount(entry.getValue().stream().count());
			transactionPerProd.setTotalFlagged(entry.getValue().stream().filter(TransactionLogEntity::getIsFraud).count());
			transactionPerProd.setTotalUnflagged(entry.getValue().stream().filter(transaction -> transaction.getIsFraud().equals(Boolean.FALSE)).count());
			
			transactionPerProducts.add(transactionPerProd);
		}
		
		log.info("[{}] Number of transactions per product before product filter", transactionPerProducts.size());
		if(Objects.nonNull(dashboardRequest.getProductCode()) && productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			
			transactionPerProducts = transactionPerProducts.stream()
					.filter(transactionPerProd -> transactionPerProd.getProductCode().equals(dashboardRequest.getProductCode())).collect(Collectors.toList());

			log.info("[{}] Number of transactions per product after product filter", transactionPerProducts.size());
			
		} else if(Objects.nonNull(dashboardRequest.getProductCode()) && !productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			log.error("ProductCode {} does not exist", dashboardRequest.getProductCode());
			throw new FraudEngineException("Product with Code " + dashboardRequest.getProductCode() + " does not exist");
		}
		
		DashBrdTransactionPerProdResponse response = new DashBrdTransactionPerProdResponse();
		response.setStatus(200);
		response.setTotalFlagged(totalFlaggedTransaction);
		response.setData(transactionPerProducts);
		
		log.info("DashBrdTransactionPerProdResponse object - {}", response);
		return response;
	}
	
	public List<DashBrdRecentTransaction> getRecentTransaction(DashboardRequest dashboardRequest, Long limit) {
		log.info("Recent Transaction Dashboard request object - {}", dashboardRequest);
		log.info("Recent Transaction Dashboard request limit - {}", limit);
		
		List<TransactionLogEntity> allTrasactions;
		if(Objects.nonNull(dashboardRequest.getProductCode()) && productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			allTrasactions = transactionLogRepository.findByProductCode(dashboardRequest.getProductCode()).stream()
					.sorted(Comparator.comparingLong(TransactionLogEntity::getId).reversed()).limit(limit).collect(Collectors.toList());
			log.info("Number of recent transactions filtered by productCode: {}", allTrasactions.size());
		
		} else if(Objects.nonNull(dashboardRequest.getProductCode()) && !productRepository.findById(dashboardRequest.getProductCode()).isPresent()) {
			log.error("ProductCode {} does not exist", dashboardRequest.getProductCode());
			throw new FraudEngineException("Product with Code " + dashboardRequest.getProductCode() + " does not exist");
		} else {
			allTrasactions = transactionLogRepository.findAll().stream()
					.sorted(Comparator.comparingLong(TransactionLogEntity::getId).reversed()).limit(limit).collect(Collectors.toList());
			log.info("Number of all recent transactions before date filter: {}", allTrasactions.size());
		}
		
		LocalDate startDate;
		LocalDate endDate;
		try {
			if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(dashboardRequest.getEndDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				if (startDate.isAfter(endDate)) {
					log.error("Start date, {}, cannot be after the end date {}", dashboardRequest.getStartDate(), dashboardRequest.getEndDate());
					throw new FraudEngineException("Start date cannot be after end date");
				}

				allTrasactions = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.sorted(Comparator.comparingLong(TransactionLogEntity::getId).reversed()).limit(limit)
						.collect(Collectors.toList());

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Number of recent transactions After date filter: {}, limit: {}", allTrasactions.size(),
						limit);
			} else if (Objects.nonNull(dashboardRequest.getStartDate()) && Objects.isNull(dashboardRequest.getEndDate())) {
				startDate = LocalDate.parse(dashboardRequest.getStartDate(), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));
				endDate = LocalDate.parse(DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT).format(LocalDate.now()), DateTimeFormatter.ofPattern(DASHBOARD_REQ_DATE_FORMAT));

				allTrasactions = allTrasactions.stream()
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(startDate) >= 0)
						.filter(transaction -> transaction.getCreatedAt().toLocalDate().compareTo(endDate) <= 0)
						.sorted(Comparator.comparingLong(TransactionLogEntity::getId).reversed()).limit(limit)
						.collect(Collectors.toList());

				log.info("Start date: {}    End date: {}", startDate, endDate);
				log.info("Number of recent transactions After date filter: {}, limit: {}", allTrasactions.size(), limit);
			} else if (Objects.isNull(dashboardRequest.getStartDate()) && Objects.nonNull(dashboardRequest.getEndDate())) {
				log.error("Start date must be specified alongside the end date {}", dashboardRequest.getEndDate());
				throw new FraudEngineException("Start date must be specified alongside the end date");
			}
		} catch (DateTimeParseException dtpEx) {
			log.error("Wrong date format was received as input: {}", dtpEx.getLocalizedMessage());
			throw new FraudEngineException("Start and End dates should be in the format " + DASHBOARD_REQ_DATE_FORMAT);
		}
		
		List<DashBrdRecentTransaction> recentTransactions = new ArrayList<>();
		allTrasactions.stream().forEach(transaction ->{
			DashBrdRecentTransaction recentTransaction = new DashBrdRecentTransaction();
			recentTransaction.setTransactionId(transaction.getTransactionId());
			if(productRepository.findById(transaction.getProductCode()).isPresent()) {
				recentTransaction.setProduct(productRepository.findById(transaction.getProductCode()).get().getName());
			}
			recentTransaction.setService(transaction.getServiceId());
			recentTransaction.setChannel(transaction.getChannel());
			recentTransaction.setAmount(transaction.getAmount());
			//recentTransaction.setSuspicionLevel(suspicionLevel)
			//recentTransaction.setAction(action)
			recentTransaction.setTransactionDate(transaction.getCreatedAt());
			
			recentTransactions.add(recentTransaction);
		});
		log.info("List of recent transactions {}", recentTransactions);
		log.info("Number of recent transactions {}", recentTransactions.size());
		return recentTransactions;
	}
	
}
