package com.senacor.tecco.ilms.wikiloader.service.integration;

import rx.Observable;

/**
 * Created by mmenzel on 25.01.2016.
 */
public interface WikipediaServiceJapi {
    String getArticle(String name);

    Observable<String> getArticleObservable(String wikiArticle);
}
