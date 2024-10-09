package com.jamie.metro.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

}
