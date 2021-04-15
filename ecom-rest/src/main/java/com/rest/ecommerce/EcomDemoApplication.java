package com.rest.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ecommerce.cache.Cache;
import com.rest.ecommerce.cache.RedisCache;
import com.rest.ecommerce.storage.StorageProperties;
import com.rest.ecommerce.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import redis.clients.jedis.Jedis;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class EcomDemoApplication{

	public static void main(String[] args) {
		SpringApplication.run(EcomDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
//			storageService.deleteAll();
			storageService.init();
		};
	}

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;
//    @Bean
    private Jedis redisCliFactory(){
        Jedis jedis = new Jedis(redisHost, redisPort);
        jedis.auth(redisPassword);
        return jedis;
    }

    @Bean
    @Autowired
    public Cache cacheObject(ObjectMapper objectMapper){
        return new RedisCache(objectMapper, redisCliFactory());
    }

}