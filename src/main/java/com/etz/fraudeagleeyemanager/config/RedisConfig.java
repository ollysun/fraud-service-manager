package com.etz.fraudeagleeyemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class RedisConfig {

    private final JedisConnectionFactory fraudEngineConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(fraudEngineConnectionFactory);
        setSerializer(template);
        return template;
    }

    private void setSerializer(RedisTemplate<String, Object> template) {
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        template.setKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setStringSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
    }

}
