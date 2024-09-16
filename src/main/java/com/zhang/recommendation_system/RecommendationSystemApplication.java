package com.zhang.recommendation_system;

import com.zhang.recommendation_system.util.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileProperties.class})
public class RecommendationSystemApplication {
    public static void main(String[] args) {
        System.out.println("111");
        SpringApplication.run(RecommendationSystemApplication.class, args);
        System.out.println("222");
    }
}
