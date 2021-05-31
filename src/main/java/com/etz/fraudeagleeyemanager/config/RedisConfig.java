package com.etz.fraudeagleeyemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean(name = "fraudEngineConnectionFactory")
    JedisConnectionFactory fraudEngineJedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration("127.0.0.1", 6379);
        //		new RedisStandaloneConfiguration("172.17.10.16", 6379);
        //redisStandaloneConfiguration.setPassword("visionsvisions");
        redisStandaloneConfiguration.setDatabase(0);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    /*
     *  Without this bean definition, the application throws an error looking for it.
     *  This is a default bean that MUST be defined with the name "redisTemplate"
     *  It is not used explicitly within the application
     *  */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
    	RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(fraudEngineJedisConnectionFactory());
        setSerializer(template);
        return template;
    }


    private void setSerializer(RedisTemplate<String, Object> template) {
        //template.setKeySerializer(new StringRedisSerializer());
        
    	//template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        //template.setValueSerializer(new JdkSerializationRedisSerializer());
        
        template.setKeySerializer(new Jackson2JsonRedisSerializer<String>(String.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
    }


}
