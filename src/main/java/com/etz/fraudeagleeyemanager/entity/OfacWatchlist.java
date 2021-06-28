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

import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import com.etz.fraudeagleeyemanager.enums.UserCategory;


@Entity
@Table(name = "ofac_watchlist")
@SQLDelete(sql = "UPDATE ofac_watchlist SET deleted = true, status=0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OfacWatchlist extends BaseAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	private UserCategory category;

	@Column(name = "comments")
	private String comments;
		
    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;

	@Column(name = "authorised", nullable = false, columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;
	
	@Column(name = "authoriser", nullable = true, length=100)
	private String authoriser;

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
