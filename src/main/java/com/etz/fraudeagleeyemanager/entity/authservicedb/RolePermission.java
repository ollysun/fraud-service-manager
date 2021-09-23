package com.etz.fraudeagleeyemanager.entity.authservicedb;

import lombok.*;
import org.hibernate.annotations.Where;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Where(clause = "deleted=false")
@Table(name = "role_permission")
public class RolePermission extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", columnDefinition = "bigint")
    private Long roleId;

    @Column(name = "permission_id", columnDefinition = "bigint")
    private Long permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermission)) return false;
        if (!super.equals(o)) return false;
        RolePermission that = (RolePermission) o;
        return roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId);
    }
}
