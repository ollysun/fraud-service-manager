package com.etz.fraudeagleeyemanager.entity.authservicedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.BaseAuditEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user",
		uniqueConstraints={
				@UniqueConstraint(name="UserEntityUniqueUsernameConstraints", columnNames = "username"),
				@UniqueConstraint(columnNames = "email", name="UserEntityUniqueEmailConstraints"),
				@UniqueConstraint(columnNames = "phone", name="UserEntityUniquePhoneConstraints"),
				@UniqueConstraint(name="UserEntityUniquePasswordConstraints", columnNames =  "password")})
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class UserEntity extends BaseAuditEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username",  length = 250)
	private String username;

	@Column(name = "password", columnDefinition = "TEXT",nullable = false)
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone", length = 200)
	private String phone;

	@Column(name = "email",  length = 250)
	private String email;

	@Column( name = "has_Role", columnDefinition = "TINYINT default true", length = 1, nullable = false)
	private Boolean hasRole = Boolean.TRUE;

	@Column(name = "has_Permission", columnDefinition = "TINYINT default true", length = 1, nullable = false)
	private Boolean hasPermission = Boolean.TRUE;

	@Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
	private Boolean status;
	
	@Column(name = "authorised", columnDefinition = "TINYINT", length = 1)
	private Boolean authorised;
	
	@Column(name = "authoriser", length=100)
	private String authoriser;

//	@ToString.Exclude
//	@OneToMany(mappedBy = "userId",fetch = FetchType.LAZY,
//			cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<ResetPasswordTokens> resetPasswordTokens =new ArrayList<>();
	
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private List<Role> roles = new ArrayList<>();

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_permission",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private List<PermissionEntity> permissionEntities = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		UserEntity that = (UserEntity) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return 1838525018;
	}
}
