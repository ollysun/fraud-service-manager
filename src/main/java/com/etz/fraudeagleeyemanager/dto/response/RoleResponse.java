package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long roleId;
    private String roleName;
    private String description;
    private List<String> permissions;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;

}
