package com.etz.fraudeagleeyemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class Oauth2ClientConfig {

	@Value("clientId")
	private String clientId;
	
	@Value("secret")
	private String clientSecret;
	
	@Value("password")
	private String grantType;
	
	@Value("http://172.17.10.83:9191/api/oauth/token")
	private String accessTokenURI;
	
	@Value("test")
	private String username;
	
	@Value("pass")
	private String password;

    @Bean
    public ResourceOwnerPasswordResourceDetails resource(){
    	ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
    	
    	resourceDetails.setClientId(clientId);
    	resourceDetails.setClientSecret(clientSecret);
    	resourceDetails.setGrantType(grantType);
    	resourceDetails.setAccessTokenUri(accessTokenURI);
    	
    	resourceDetails.setUsername(username);
    	resourceDetails.setPassword(password);
    	resourceDetails.setAuthenticationScheme(AuthenticationScheme.header); //this again depends on the OAuth2 server specifications
    	
    	return resourceDetails;
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
        return oauth2RestTemplate;
    }


}
