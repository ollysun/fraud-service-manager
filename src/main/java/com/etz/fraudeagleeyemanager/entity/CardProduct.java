package com.etz.fraudeagleeyemanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "card_product", uniqueConstraints = @UniqueConstraint(name="UC_CARD_PRODUCT",
        columnNames = {"card_id"}))
@SQLDelete(sql = "UPDATE card_product SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@IdClass(CardProductId.class)
public class CardProduct extends BaseAuditVersionEntity<CardProductId> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Id
    @Column(name = "product_code", nullable = false, columnDefinition="VARCHAR(100)")
    private String productCode;

    @Id
    @Column(name = "card_id", nullable = false,  columnDefinition = "bigint")
    private Long cardId;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    @ToString.Exclude
    private Card card;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productCode")
    @JoinColumn(name = "product_code")
    @ToString.Exclude
    private ProductEntity productEntity;


	@Override
	public CardProductId getId() {
		return new CardProductId(id, productCode, cardId);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CardProduct that = (CardProduct) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(productCode, that.productCode)) return false;
        return Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + (Objects.hashCode(productCode));
        result = 31 * result + (Objects.hashCode(cardId));
        return result;
    }
}
