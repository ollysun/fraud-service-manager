package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "internal_watchlist")
@SQLDelete(sql = "UPDATE internal_watchlist SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InternalWatchlist extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Bvn number cannot be empty")
	@Column(name = "bvn", unique = true, nullable = false, length=11)
	private String bvn;

	@Column(name = "comments")
	private String comments;
		
	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	//@Enumerated(EnumType.ORDINAL)
	private Boolean status;

	@Column(nullable = false, name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;
	
	@Column(name = "authoriser", nullable = true, length=100) //at creation authoriser will not be provided
	private String authoriser;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InternalWatchlist that = (InternalWatchlist) o;

		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
