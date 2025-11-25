package com.caixabank.transaction.client;

import com.caixabank.transaction.dtos.AccountResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccountClient {
    private final WebClient webClient;

    public AccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountResponse getMainAccount(Long userId) {
        return webClient
                .get()
                .uri("/accounts/main")
                .header("X-User-Id", String.valueOf(userId))
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .block();
    }

    public AccountResponse getAccount(Long userId, Long id) {
        return webClient
                .get()
                .uri("/accounts/{id}", id)
                .header("X-User-Id", String.valueOf(userId))
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .block();
    }
}
