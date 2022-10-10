package com.etz.fraudeagleeyemanager.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.authorisation-service")
public class AuthServerMandatoryURL {

	@NotBlank(message = "Update User authoriser URL is required!")
	private String userAuthoriserUrl;
	
	@NotBlank(message = "Update Role authoriser URL is required!")
	private String roleAuthoriserUrl;
	
	@NotBlank(message = "Get RoleIdsBy PermissionName URL is required!")
	private String roleIdsByPermissionNameUrl;
}
