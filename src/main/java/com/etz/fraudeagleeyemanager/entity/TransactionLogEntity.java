package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "transaction_log")
@Data
public class TransactionLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "transaction_id", unique = true, nullable = false)
	private String transactionId;

	@Column(name = "source_url", nullable = false)
	private String sourceURL;
	
	@Column(name = "is_fraud", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean isFraud = false;

	@Column(name = "flagged_rule")
	private Long flaggedRule;

	@Column(name = "product_code", nullable = false)
	private String productCode;
	
	@Column(name = "service_id", nullable = false)
	private String serviceId;
	
	@Column(name = "use_card", columnDefinition = "TINYINT", length = 1)
	private Boolean useCard = false;

	@Column(name = "card_id")
	private Long cardId;

	@Column(name = "use_account", columnDefinition = "TINYINT", length = 1)
	private Boolean useAccount = false;
	
	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "channel", nullable = false, length = 50)
	private String channel;

	@Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(20,2)")
	private BigDecimal amount;

	@Column(name = "request_dump", nullable = false)
	@Type(type = "text")
	private String requestDump;
		
	@Column(name = "created_by", nullable = false, length = 45)
	private String createdBy;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
}
