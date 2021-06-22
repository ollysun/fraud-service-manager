package com.etz.fraudeagleeyemanager.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Component
public class BaseInterceptorConfig extends WebMvcConfigurerAdapter {
	   
	 @Autowired
	 BaseInterceptor productInterceptor;
	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(productInterceptor);
	   }
}
