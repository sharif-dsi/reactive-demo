package com.bk.nz.reactivedemo.controller;

import com.bk.nz.reactivedemo.request.SimpleRequest;
import com.bk.nz.reactivedemo.response.SimpleResponse;
import com.bk.nz.reactivedemo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("api")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping(value = "/reactive/no-executor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SimpleResponse> testReactiveWithoutExecutor(@RequestBody SimpleRequest simpleRequest) {
        return testService.testReactiveWithWebclient(simpleRequest);
    }


    @PostMapping(value = "/reactive/custom-executor",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SimpleResponse> testReactiveWithCustomExecutor(@RequestBody SimpleRequest simpleRequest) {
        var futureResponse = testService.testReactiveWithExecutor(simpleRequest);
        return Mono.fromFuture(futureResponse);
    }

}
