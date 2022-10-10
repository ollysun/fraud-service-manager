package com.etz.fraudeagleeyemanager.dto.response;

import lombok.Data;

@Data
public class AuthorizationMetaData {
    	
    private String access_token;
	private Integer expires_in;
	private Integer refresh_expires_in;
	private String token_type;
	private Integer id_token;
	private Integer not_before_policy;
	private String session_state;
	private String scope;
	private String refresh_token;
	
}
