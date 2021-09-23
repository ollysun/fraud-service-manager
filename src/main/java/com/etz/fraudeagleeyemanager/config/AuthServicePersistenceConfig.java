package com.etz.fraudeagleeyemanager.config;

import java.util.HashMap;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories(
		  basePackages = "com.etz.fraudeagleeyemanager.repository.authservicedb", 
		  entityManagerFactoryRef = "authServiceEntityManager", 
		  transactionManagerRef = "authServiceTransactionManager")
@RequiredArgsConstructor
public class AuthServicePersistenceConfig {

    
    private final Environment env;
    
    @Bean
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource authserviceDataSource() {
        //DataSourceBuilder.create().build();
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.second-datasource.url"));
        dataSource.setUsername(env.getProperty("spring.second-datasource.username"));
        dataSource.setPassword(env.getProperty("spring.second-datasource.password"));
    	dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
    	return dataSource;
    }
    
    
    @Bean
    @PersistenceContext(unitName = "secondary")
    public LocalContainerEntityManagerFactoryBean authServiceEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(authserviceDataSource());
        em.setPackagesToScan("com.etz.fraudeagleeyemanager.entity.authservicedb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", "none");
        em.setJpaPropertyMap(properties);

        return em;
    }
    
    @Bean
    public PlatformTransactionManager authServiceTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(authServiceEntityManager().getObject());
        return transactionManager;
    }
    
}
