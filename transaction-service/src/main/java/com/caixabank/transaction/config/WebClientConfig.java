package com.caixabank.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${ACCOUNT_SERVICE_HOST}")
    private String accountHost;

    @Value("${ACCOUNT_SERVICE_PORT}")
    private int accountPort;

    @Bean
    public WebClient accountWebClient() {
        return WebClient.builder()
                .baseUrl("http://" + accountHost + ":" + accountPort)
                .build();
    }
}
