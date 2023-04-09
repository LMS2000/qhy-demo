package com.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 更改RedisTemplate默认序列化方式
 * @author zdh
 */
@Configuration
public class RedisConfig {

   @Bean("redisTemplate")
   public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
   {
       //创建template
       RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
       //设置连接工厂
       redisTemplate.setConnectionFactory(redisConnectionFactory);
       //设置序列化工具
       GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
       //key和hashKey采用String序列化
       redisTemplate.setKeySerializer(RedisSerializer.string());
       redisTemplate.setHashKeySerializer(RedisSerializer.string());
       //value和hashValue用JSON序列化
       redisTemplate.setValueSerializer(jsonRedisSerializer);
       redisTemplate.setHashValueSerializer(jsonRedisSerializer);
       return redisTemplate;
   }
}