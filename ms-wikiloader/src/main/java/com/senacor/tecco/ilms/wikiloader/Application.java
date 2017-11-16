package com.senacor.tecco.ilms.wikiloader;

import com.senacor.tecco.ilms.common.exception.EnableGlobalExceptionHandling;
import com.senacor.tecco.ilms.common.rxjava.EnableRxJavaServiceResponseMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableGlobalExceptionHandling
@EnableCircuitBreaker
@EnableRxJavaServiceResponseMapping
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
