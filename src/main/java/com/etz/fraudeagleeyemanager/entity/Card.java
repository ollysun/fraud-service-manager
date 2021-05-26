package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.CardBrand;
import com.etz.fraudeagleeyemanager.constant.CardType;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.util.Set;

@Entity
@Table(name = "card", 
		uniqueConstraints = @UniqueConstraint(name="UC_CARD",
				columnNames = {"card_bin", "iso_country_code"}))
@SQLDelete(sql = "UPDATE card SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@ToString(exclude = { "cards" })
@RequiredArgsConstructor
public class Card extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cardholder_name")
	private String cardholderName;

	@NotBlank(message = "Card bin cannot be empty")
	@Column(name = "card_bin")
	private Integer cardBin;

	@NotBlank(message = "Card brand cannot be empty")
	@Column(name = "card_brand")
	@Enumerated(EnumType.STRING)
	private CardBrand cardBrand;

	@NotBlank(message = "Card Type cannot be empty")
	@Column(name = "card_type")
	@Enumerated(EnumType.STRING)
	private CardType cardType;

	@NotBlank(message = "Card Expiry cannot be empty")
	@Column(name = "card_expiry")
	private String cardExpiry;

	@NotBlank(message = "Card cvv cannot be empty")
	@Column(name = "card_cvv")
	private Integer cardCVV;

	@NotBlank(message = "Issuer Code cannot be empty")
	@Column(name = "issuer_code")
	private String issuerCode;

	@NotBlank(message = "Issuer name cannot be empty")
	@Column(name = "issuer_name")
	private String issuerName;

	@NotBlank(message = "Iso country cannot be empty")
	@Column(name = "iso_country")
	private String isoCountry;

	@NotBlank(message = "Iso Country Code cannot be empty")
	@Column(name = "iso_country_code")
	private Integer isoCountryCode;

	@Column(name = "suspicion_count")
	private Integer suspicionCount;

	@Column(name = "block_reason")
	private String blockReason;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@OneToMany(mappedBy = "card",fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<CardProduct> cards;

}

