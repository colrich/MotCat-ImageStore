package io.gridbug.cats.motcat;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MotCatConfig extends AbstractCloudConfig {

	@Bean
	public RedisConnectionFactory redisFactory() {
	    return connectionFactory().redisConnectionFactory();
	}
	
	@Bean
	RedisTemplate< String, byte[] > redisTemplate() {
		final RedisTemplate< String, byte[] > template =  new RedisTemplate< String, byte[] >();
		template.setConnectionFactory( redisFactory() );
		template.setKeySerializer( new StringRedisSerializer() );
		template.setHashValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
		template.setValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
		return template;
	}
	
	
}
