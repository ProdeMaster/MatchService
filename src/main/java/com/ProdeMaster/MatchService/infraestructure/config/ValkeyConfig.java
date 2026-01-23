package com.ProdeMaster.MatchService.infraestructure.config;

import com.ProdeMaster.MatchService.domain.model.Match;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

@Configuration
public class ValkeyConfig {

    @Bean
    public ReactiveRedisTemplate<String, Match> reactiveRedisMatchTemplate(
            @NonNull ReactiveRedisConnectionFactory connectionFactory) {

        Jackson2JsonRedisSerializer<Match> serializer = new Jackson2JsonRedisSerializer<>(Match.class);

        RedisSerializationContext<String, Match> context = RedisSerializationContext
                .<String, Match>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisObjectTemplate(
            @NonNull ReactiveRedisConnectionFactory connectionFactory) {

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
