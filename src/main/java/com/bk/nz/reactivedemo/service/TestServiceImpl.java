package com.bk.nz.reactivedemo.service;

import com.bk.nz.reactivedemo.request.SimpleRequest;
import com.bk.nz.reactivedemo.response.SimpleResponse;
import com.bk.nz.reactivedemo.util.FutureExecutorService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        return futureExecutorService.execute(() -> {
            var httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_PATH + apiPath + "/" + simpleRequest.getDelayTime()))
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();

            var httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return (new Gson()).fromJson(httpResponse.body(), SimpleResponse.class);
        });
    }

}
