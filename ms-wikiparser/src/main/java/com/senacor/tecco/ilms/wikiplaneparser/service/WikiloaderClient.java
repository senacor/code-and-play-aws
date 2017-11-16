package com.senacor.tecco.ilms.wikiplaneparser.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rx.Single;

@FeignClient(value = "wikiloader", url = "${wikiloader.url}")
interface WikiloaderClient {

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    }, observableExecutionMode = ObservableExecutionMode.LAZY)
    @RequestMapping(value = "/services/wiki/article/{articleName}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    Single<ServiceResponse<String>> fetchArticle(@PathVariable("articleName") String articleName);
}
