package io.gridbug.cats.motcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@SpringBootApplication
public class ImageStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageStoreApplication.class, args);
	}
	
}
