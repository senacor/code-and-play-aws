package com.senacor.tecco.ilms.wikiloader;

import com.senacor.tecco.ilms.common.context.EnableServicesRequestContext;
import com.senacor.tecco.ilms.common.exception.EnableGlobalExceptionHandling;
import com.senacor.tecco.ilms.common.rxjava.EnableRxJavaServiceResponseMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableGlobalExceptionHandling
@EnableServicesRequestContext
@EnableCircuitBreaker
@EnableEurekaClient
@EnableRxJavaServiceResponseMapping
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
