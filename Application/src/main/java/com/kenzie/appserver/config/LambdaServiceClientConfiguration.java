package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public LambdaRecipeServiceClient referralRecipeServiceClient() {
        return new LambdaRecipeServiceClient();
    }
}
