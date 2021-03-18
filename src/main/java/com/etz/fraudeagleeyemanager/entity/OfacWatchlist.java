package com.etz.fraudeagleeyemanager.entity;

import com.etz.fraudeagleeyemanager.constant.BooleanStatus;
import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.constant.UserCategory;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Entity
@Table(name = "ofac_watchlist")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OfacWatchlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "Full name cannot be empty")
	@Column(name = "fullname")
	private String fullname;

	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	private UserCategory category;

	@NotBlank(message = "Comments cannot be empty")
	@Column(name = "comments")
	private String comments;
		
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@Column(name = "authorised")
	@Enumerated(EnumType.ORDINAL)
	private BooleanStatus authorised;
	
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
		OfacWatchlist that = (OfacWatchlist) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
