package com.etz.fraudeagleeyemanager.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableResourceServer
@EnableWebSecurity
//@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private static final String SIGNING_KEY = "AuthETransactNgView2021";
	
    @Value("${security.oauth2.resource.id}")
    private String oauth2ResourceId;

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String oauth2VerifierKey;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resources.resourceId(oauth2ResourceId).tokenStore(tokenStore());
    	resources.resourceId(oauth2ResourceId).tokenServices(tokenServices(tokenStore())).tokenStore(tokenStore());
    }

    
    @Bean
    public DefaultTokenServices tokenServices(final TokenStore tokenStore) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        //security flaws
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/health", "/info", "/trace", "/metrics", "/monitoring", "/webjars/**","/swagger.html", "/v1/static-view/**", "api/test/**", "/v1/report/download")
                .permitAll();
        //http.addFilterBefore(new CustomFilter(), OAuth2LoginAuthenticationFilter.class);

        // Patterns to be added once we can test with a token
        //http.authorizeRequests().antMatchers("/v1/**").authenticated();
        http.authorizeRequests().antMatchers("/**").permitAll();
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(new CustomJwtAccessTokenConverter());
        //converter.setVerifierKey(oauth2VerifierKey);
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("v1/**", configuration);
        return source;
    }

}
