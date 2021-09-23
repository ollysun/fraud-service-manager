package com.etz.fraudeagleeyemanager.entity.eagleeyedb;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.*;
import java.util.Set;

@Entity
@Table(name = "card", 
		uniqueConstraints = @UniqueConstraint(name="UC_CARD",
				columnNames = {"card_bin", "iso_country_code"}))
@SQLDelete(sql = "UPDATE card SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cardholder_name")
	private String cardholderName;

	@Column(name = "card_bin")
	private String cardBin;

	@Column(name = "card_brand")
	private String cardBrand;

	@Column(name = "card_type")
	private String cardType;

	@Column(name = "card_expiry")
	private String cardExpiry;

	@Column(name = "card_cvv")
	private Integer cardCVV;

	@Column(name = "issuer_code")
	private String issuerCode;

	@Column(name = "issuer_name")
	private String issuerName;

	@Column(name = "iso_country")
	private String isoCountry;

	@Column(name = "iso_country_code")
	private String isoCountryCode;

	@Column(name = "suspicion_count")
	private Integer suspicionCount;

	@Column(name = "block_reason")
	private String blockReason;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@ToString.Exclude
	@OneToMany(mappedBy = "card",fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private Set<CardProduct> cardProducts;

}

