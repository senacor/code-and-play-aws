package com.senacor.tecco.ilms.wikiplaneparser.service;

import rx.Single;

/**
 * @author Andreas Keefer
 */
public interface FetchArticleService {

    /**
     * fetch article from wikipedia
     *
     * @param name article Name
     * @return article in media wiki format
     */
    Single<String> fetchArticle(String name);
}
