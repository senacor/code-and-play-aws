package com.senacor.tecco.ilms.wikiloader.service.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rx.Observable;

import static com.senacor.tecco.ilms.common.utils.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
@Component
@Profile("!mock")
public class WikipediaServiceJapiImpl implements WikipediaServiceJapi {

    @Autowired
    private MediawikiAdapter wiki;

    public WikipediaServiceJapiImpl() {
    }

    protected String getPageContent(String name) throws Exception {
        return wiki.getPageContent(name);
    }

    @Override
    public String getArticle(String name) {
        try {
            final long start = System.currentTimeMillis();
            String res = getPageContent(name);
            print("profiling getArticle(" + name + "): " + (System.currentTimeMillis() - start) + "ms");
            return res;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Observable<String> getArticleObservable(String wikiArticle) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(getArticle(wikiArticle));
                subscriber.onCompleted();
            } catch (RuntimeException e) {
                subscriber.onError(e);
            }
        });
    }
}
