package org.example.untitled_testai.controller;


import org.example.untitled_testai.dto.DeepSeekRequest;
import org.example.untitled_testai.dto.DeepSeekResponse;
import org.example.untitled_testai.service.DeepSeekService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final DeepSeekService deepSeekService;

    public AIController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    @PostMapping("/chat")
    public Mono<DeepSeekResponse> chat(@RequestBody DeepSeekRequest request) {
        return deepSeekService.chatCompletion(request);
    }
}
