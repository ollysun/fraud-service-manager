package com.etz.fraudeagleeyemanager.config;

import java.util.Map;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import com.etz.fraudeagleeyemanager.util.RequestUtil;

public class CustomJwtAccessTokenConverter extends DefaultAccessTokenConverter {

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		OAuth2Authentication authentication = super.extractAuthentication(map);
		authentication.setDetails(map);
		RequestUtil.setAccessTokenClaim(authentication);
		return authentication;
	}

}
