package org.example.untitled_testai.service;

import org.example.untitled_testai.dto.DeepSeekRequest;
import org.example.untitled_testai.dto.DeepSeekResponse;
import reactor.core.publisher.Mono;

public interface DeepSeekService {
    Mono<DeepSeekResponse> chatCompletion(DeepSeekRequest request);
}
