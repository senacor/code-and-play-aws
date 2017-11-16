package com.senacor.tecco.ilms.common.context;

import com.senacor.tecco.ilms.common.exception.EnableGlobalExceptionHandling;
import com.senacor.tecco.ilms.common.rxjava.EnableRxJavaServiceResponseMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableGlobalExceptionHandling
@EnableServicesRequestContext
@EnableCircuitBreaker
@EnableRxJavaServiceResponseMapping
public class ContextTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContextTestApplication.class, args);
    }
}
