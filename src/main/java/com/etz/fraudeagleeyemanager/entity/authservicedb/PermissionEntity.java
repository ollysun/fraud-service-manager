package com.etz.fraudeagleeyemanager.entity.authservicedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.BaseEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@Where(clause = "deleted=false")
@RequiredArgsConstructor
public class PermissionEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private long id;

    @Column(name = "name", unique = true, length = 200)
    private String name;

    @Column(nullable = false, name = "status", columnDefinition = "TINYINT", length = 1)
    private Boolean status;

    @ToString.Exclude
    @ManyToMany(mappedBy = "rolePermissionEntities", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "permissionEntities",fetch = FetchType.LAZY)
    private List<UserEntity> userEntities = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionEntity)) return false;
        if (!super.equals(o)) return false;
        PermissionEntity that = (PermissionEntity) o;
        return id == that.id && name.equals(that.name) && status.equals(that.status) && roles.equals(that.roles) && userEntities.equals(that.userEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, status, roles, userEntities);
    }
}
