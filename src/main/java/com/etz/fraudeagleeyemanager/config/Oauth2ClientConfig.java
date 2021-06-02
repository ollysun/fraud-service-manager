package com.etz.fraudeagleeyemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@EnableOAuth2Client
public class Oauth2ClientConfig {

    //@Value("${spring.authentication-server.create-token-url}")
    //private String accessTokenURI;

//    @Bean
//    public ClientCredentialsResourceDetails resourceDetails(){
//        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
//        resourceDetails.setClientId("");
//        resourceDetails.setClientSecret("");
//        resourceDetails.setGrantType("");
//        resourceDetails.setAccessTokenUri("");

//        resourceDetails.setUsername("admin");
//        resourceDetails.setPassword("pass");
//        resourceDetails.setAuthenticationScheme(AuthenticationScheme.header); //this again depends on the OAuth2 server specifications
//
//        return resourceDetails;
//    }
//
//    @Bean
//    public OAuth2RestTemplate oAuth2RestTemplate() {
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(6000);
//        requestFactory.setReadTimeout(6000);
//        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
//        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
//        AccessTokenRequest atr = new DefaultAccessTokenRequest();
//        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resourceDetails(), new DefaultOAuth2ClientContext(atr));
//        oauth2RestTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
//        oauth2RestTemplate.setRequestFactory(requestFactory);
//        return oauth2RestTemplate;
//    }


}
