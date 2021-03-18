package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_rule", 
uniqueConstraints = @UniqueConstraint(
		columnNames = {"product_code", "rule_id"}))
@Getter
@Setter
@RequiredArgsConstructor
public class ProductRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Email(message="please enter valid email")
	@NotBlank(message = "Rule Id cannot be empty")
	@Column(name = "rule_id")
	private Long ruleId;
	
	@Column(name = "notify_admin", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus notifyAdmin;
	
	@Column(name = "email_group")
	private Long emailGroup;
			
	@Column(nullable = false, name = "notify_customer", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus notifyCustomer;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status authorised;

	@NotBlank(message = "Created By cannot be empty")
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "code", nullable = false)
	private Product product;

	@Override
	public String toString() {
		return "ProductRule{" +
				"id=" + id +
				", ruleId=" + ruleId +
				", notifyAdmin=" + notifyAdmin +
				", emailGroup=" + emailGroup +
				", notifyCustomer=" + notifyCustomer +
				", status=" + status +
				", authorised=" + authorised +
				", createdBy='" + createdBy + '\'' +
				", createdAt=" + createdAt +
				", updatedBy='" + updatedBy + '\'' +
				", updatedAt=" + updatedAt +
				", product=" + product +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductRule that = (ProductRule) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
