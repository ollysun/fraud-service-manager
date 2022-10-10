package com.etz.fraudeagleeyemanager.dto.response;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class UserResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Boolean hasRole;
    private Long roleId;
    private Boolean hasPermission;
    private List<String> permissionNames;
    private Boolean status;
    private String createdBy;
    private LocalDateTime createdAt;
    private Long optLock;
}
