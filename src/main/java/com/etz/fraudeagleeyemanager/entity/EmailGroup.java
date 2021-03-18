package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_group")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EmailGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "group name cannot be empty")
	@Column(name = "group_name", unique = true)
	private String groupName;

	@Email(message="please enter valid email")
	@NotBlank(message = "email cannot be empty")
	@Column(name = "email")
	private String email;
	
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmailGroup that = (EmailGroup) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
