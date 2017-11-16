package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import rx.Single;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Andreas Keefer
 */
@Component
@Profile("mock")
public class FetchArticleServiceMock implements FetchArticleService {

    private static final Logger logger = LoggerFactory.getLogger(FetchArticleServiceMock.class);

    public Single<String> fetchArticle(String name) {
        return Single.create(singleSubscriber -> {
            try {
                DefaultResourceLoader loader = new DefaultResourceLoader();
                String resourceName = "mock/" + name + ".txt";
                Resource resource = loader.getResource("classpath:" + resourceName);
                notNull(resource, "no mockdata found for " + name);
                Path path = Paths.get(resource.getURI());
                logger.info("load mock data: " + path.toString());
                String res = new String(Files.readAllBytes(path));
                logger.info("fetched mock article '{}'", name);
                singleSubscriber.onSuccess(res);
            } catch (Exception e) {
                logger.warn("no mockdata for article '" + name + "' found", e);
                singleSubscriber.onError(e);
            }
        });
    }
}
