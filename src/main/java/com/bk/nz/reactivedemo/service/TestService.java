package com.bk.nz.reactivedemo.service;

import com.bk.nz.reactivedemo.request.SimpleRequest;
import com.bk.nz.reactivedemo.response.SimpleResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface TestService {

    Mono<SimpleResponse> testReactiveWithWebclient(SimpleRequest simpleRequest);
    CompletableFuture<SimpleResponse> testReactiveWithExecutor(SimpleRequest simpleRequest);

}
