package org.example.untitled_testai.service.impl;

import org.example.untitled_testai.dto.DeepSeekRequest;
import org.example.untitled_testai.dto.DeepSeekResponse;
import org.example.untitled_testai.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {
    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public DeepSeekServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<DeepSeekResponse> chatCompletion(DeepSeekRequest request) {

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeepSeekResponse.class);
    }
}