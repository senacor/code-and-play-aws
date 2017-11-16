package com.senacor.tecco.ilms.wikiloader.service.integration;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import rx.Single;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
@Component
@Profile("mock")
public class WikipediaServiceJapiMock extends WikipediaServiceJapiImpl {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaServiceJapiMock.class);

    private final int delay;

    public WikipediaServiceJapiMock() {
        this(100);
    }

    public WikipediaServiceJapiMock(int delay) {
        this.delay = delay;
    }

    @Override
    protected String getPageContent(String name) throws Exception {
        Thread.sleep(delay);
        try {
            return fetchArticle(name).toBlocking().value();
        } catch (Exception e) {
            return fetchArticle("Boeing 777").toBlocking().value();
        }
    }

    private Single<String> fetchArticle(String name) {
        return Single.create(singleSubscriber -> {
            try {
                DefaultResourceLoader loader = new DefaultResourceLoader();
                String resourceName = "mock/" + name + ".txt";
                Resource resource = loader.getResource("classpath:" + resourceName);
                notNull(resource, "no mockdata found for " + name);
                logger.info("load mock data: " + resource.getURL());
                String res;
                try (InputStream inputStream = resource.getInputStream()) {
                    res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                }
                logger.info("fetched mock article '{}'", name);
                singleSubscriber.onSuccess(res);
            } catch (Exception e) {
                logger.warn("no mockdata for article '" + name + "' found", e);
                singleSubscriber.onError(e);
            }
        });
    }
}
