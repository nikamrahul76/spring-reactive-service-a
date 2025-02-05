package com.rn.study.api.service_a.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class HelloController {
    private final WebClient webClient; // or RestTemplate

    public HelloController(@LoadBalanced WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "serviceBCallCircuitBreaker", fallbackMethod = "callBFallback")
    @GetMapping("/call")
    public Mono<String> callB() {
       return webClient
                .get()
                .uri("http://192.168.0.167:8082/hello")
                .retrieve()
               .onStatus(HttpStatusCode::is4xxClientError, response ->
                       Mono.error(new RuntimeException("4xx error")))
               .onStatus(HttpStatusCode::is5xxServerError, response ->
                       Mono.error(new RuntimeException("5xx error")))
               .bodyToMono(String.class);
       // return Mono.just("Hello from a");
    }

    // Fallback method must match return type & accept Throwable as last parameter
    public Mono<String> callBFallback(Throwable t) {
        return Mono.just("Service B is currently unavailable. Please try again later.");
    }

}