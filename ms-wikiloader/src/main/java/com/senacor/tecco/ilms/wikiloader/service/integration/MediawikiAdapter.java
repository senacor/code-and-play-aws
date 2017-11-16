package com.senacor.tecco.ilms.wikiloader.service.integration;

import com.bitplan.mediawiki.japi.Mediawiki;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author Andreas Keefer
 */
@Component
public class MediawikiAdapter {

    @Autowired
    private Mediawiki wiki;

    @Cacheable("wiki.article")
    @HystrixCommand(commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public String getPageContent(String name) throws Exception {
        return wiki.getPageContent(name);
    }
}
