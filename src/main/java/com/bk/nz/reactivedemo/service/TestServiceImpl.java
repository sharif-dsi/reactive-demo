package com.bk.nz.reactivedemo.service;

import com.bk.nz.reactivedemo.request.SimpleRequest;
import com.bk.nz.reactivedemo.response.SimpleResponse;
import com.bk.nz.reactivedemo.util.FutureExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

    private static final String API_BASE_PATH = "https://coreproxy.labs.bka.sh";
    private static final String apiPath = "/dummy-api";


    @Autowired
    FutureExecutorService futureExecutorService;

    private final WebClient webClient;
    private final HttpClient httpClient;

    public TestServiceImpl() {
        this.webClient = WebClient.builder().baseUrl(API_BASE_PATH).build();
        this.httpClient = HttpClient.newHttpClient();
    }


    @Override
    public Mono<SimpleResponse> testReactiveWithWebclient(SimpleRequest simpleRequest) {
        return this.webClient.post()
                .uri(apiPath + "/" + simpleRequest.getDelayTime())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SimpleResponse.class);
    }


    @Override
    public CompletableFuture<SimpleResponse> testReactiveWithExecutor(SimpleRequest simpleRequest) {

        return futureExecutorService.execute(() -> this.webClient.post()
                .uri(apiPath + "/" + simpleRequest.getDelayTime())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SimpleResponse.class).block());
    }

}
