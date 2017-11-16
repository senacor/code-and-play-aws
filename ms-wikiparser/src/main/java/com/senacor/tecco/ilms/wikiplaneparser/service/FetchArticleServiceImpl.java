package com.senacor.tecco.ilms.wikiplaneparser.service;

import com.senacor.tecco.ilms.common.exception.ApplicationException;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.wikiplaneparser.common.RequestContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rx.Single;

/**
 * @author Andreas Keefer
 */
@Component
@Profile("!mock")
public class FetchArticleServiceImpl implements FetchArticleService {

    private static final Logger logger = LoggerFactory.getLogger(FetchArticleServiceImpl.class);

    @Autowired
    private WikiloaderClient wikiloaderClient;

    public Single<String> fetchArticle(String name) {
        return wikiloaderClient.fetchArticle(name)
            .map(res -> {
                if (!res.isSuccessful()) {
                    throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, res.getMessages());
                }
                return res.getResult();
            });
    }
}
