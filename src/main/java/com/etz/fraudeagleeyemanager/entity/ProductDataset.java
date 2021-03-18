package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.io.*;

@Entity
@Table(name = "product_dataset", uniqueConstraints = @UniqueConstraint(
		columnNames = {"product_code"}))
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductDataset implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "product_code")
	private String productCode;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "field_name")
	private String fieldName;

	@NotBlank(message = "Account number cannot be empty")
	@Column(name = "data_type")
	private String dataType;

	@Column(nullable = false, name = "mandatory", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus mandatory;
	
	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status authorised;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
}
