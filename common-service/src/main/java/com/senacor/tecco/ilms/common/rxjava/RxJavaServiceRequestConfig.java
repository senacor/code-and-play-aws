package com.senacor.tecco.ilms.common.rxjava;

import io.jmnarloch.spring.boot.rxjava.config.RxJava;
import io.jmnarloch.spring.boot.rxjava.mvc.ObservableReturnValueHandler;
import org.springframework.context.annotation.Bean;

/**
 * @author Andreas Keefer
 */
public class RxJavaServiceRequestConfig {

    @Bean
    @RxJava
    public ObservableReturnValueHandler observableReturnValueHandler() {
        return new ServiceResponseObservableReturnValueHandler();
    }
// sthet momentan in konflikt mit org.springframework.cloud.netflix.rx.SingleReturnValueHandler
//    @Bean
//    @RxJava
//    public SingleReturnValueHandler singleReturnValueHandler() {
//        return new ServiceResponseSingleReturnValueHandler();
//    }
}
