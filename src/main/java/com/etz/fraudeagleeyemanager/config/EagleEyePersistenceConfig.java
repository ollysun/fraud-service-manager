package com.etz.fraudeagleeyemanager.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
		  basePackages = "com.etz.fraudeagleeyemanager.repository.eagleeyedb", 
		  entityManagerFactoryRef = "eagleeyeEntityManager", 
		  transactionManagerRef = "eagleeyeTransactionManager")
@RequiredArgsConstructor
public class EagleEyePersistenceConfig {

    
    private final Environment env;
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource eagleEyeDataSource() {
    	//DataSource ds =  DataSourceBuilder.create().build();
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
    	dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
    	return dataSource;
    }
    
    
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean eagleeyeEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(eagleEyeDataSource());
        em.setPersistenceUnitName("primary");
        em.setPackagesToScan("com.etz.fraudeagleeyemanager.entity.eagleeyedb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        em.setJpaPropertyMap(properties);

        return em;
    }
    
    @Bean
    @Primary
    public PlatformTransactionManager eagleeyeTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(eagleeyeEntityManager().getObject());
        return transactionManager;
    }
    
}
