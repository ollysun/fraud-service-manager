package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Channel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_log")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TransactionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "transaction_id", unique = true)
	private String transactionId;

	@NotBlank(message = "Source Url cannot be empty")
	@Column(name = "source_url")
	private String sourceURL;
	
	@Column(name = "is_fraud")
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus isFraud;

	@Column(name = "product_id")
	private String productId;
	
	@Column(name = "use_card")
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus useCard;

	@Column(name = "use_account")
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus useAccount;

	@Column(name = "channel")
	@Enumerated(EnumType.STRING)
	private Channel channel;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "request_dump")
	private String requestDump;
		
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "flagged_rule",  referencedColumnName="id",nullable = false)
	private ProductRule productRule;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_code", referencedColumnName="code", nullable = false)
	private Product product;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "card_id", referencedColumnName="id", nullable = false)
	private Card card;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", referencedColumnName="id")
	private Account account;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransactionLog that = (TransactionLog) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
