package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.etz.fraudeagleeyemanager.constant.Status;
import com.etz.fraudeagleeyemanager.constant.UserCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "ofac_watchlist")
@SQLDelete(sql = "UPDATE ofac_watchlist SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OfacWatchlist extends BaseAuditEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Full name cannot be empty")
	@Column(name = "full_name")
	private String fullName;

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
	private Boolean authorised;

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
