package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.LambdaRecipeServiceClient;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public LambdaServiceClient referralServiceClient() {
        return new LambdaServiceClient();
    }

    @Bean
    public LambdaRecipeServiceClient referralRecipeServiceClient() {
        return new LambdaRecipeServiceClient();
    }
}
