package com.etz.fraudeagleeyemanager.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public CloseableHttpClient httpClient() {
	    HttpClientBuilder builder = HttpClientBuilder.create();
		return builder.build();
	}
	
	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setHttpClient(httpClient());
	    return clientHttpRequestFactory;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate(clientHttpRequestFactory());
	}
}
