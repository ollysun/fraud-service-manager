package com.etz.fraudeagleeyemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.web.util.DefaultUriBuilderFactory;

//@Configuration
//@EnableOAuth2Client
public class Oauth2ClientConfig {

	//@Value("${spring.product-server.client-id}")
	private String clientId;
	
	//@Value("${spring.product-server.client-secret}")
	private String clientSecret;
	
	//@Value("${spring.product-server.grant-type}")
	private String grantType;
	
	//@Value("${spring.product-server.create-token-url}")
	private String accessTokenURI;
	
	//@Value("${spring.product-server.username}")
	private String username;
	
	//@Value("${spring.product-server.password}")
	private String password;

    @Bean
    public ResourceOwnerPasswordResourceDetails resourceDetails(){
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
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(6000);
        requestFactory.setReadTimeout(6000);
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resourceDetails(), new DefaultOAuth2ClientContext(atr));
        oauth2RestTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        oauth2RestTemplate.setRequestFactory(requestFactory);
        return oauth2RestTemplate;
    }


}
