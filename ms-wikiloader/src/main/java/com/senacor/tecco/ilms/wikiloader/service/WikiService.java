package com.senacor.tecco.ilms.wikiloader.service;

import com.senacor.tecco.ilms.wikiloader.service.integration.MediaWikiTextParser;
import com.senacor.tecco.ilms.wikiloader.service.integration.WikipediaServiceJapi;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.functions.Action1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static com.senacor.tecco.ilms.common.utils.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
@Component
public class WikiService {

    @Autowired
    private MediaWikiTextParser parser;

    /**
     * when true, the results from fetchArticleObservable will be recorded for use in MOCKMODE;
     */
    private boolean record = false;

    @Autowired
    private WikipediaServiceJapi wikiServiceJapi;

    private ExecutorService pool = Executors.newFixedThreadPool(4);

    public WikiService() {
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formated string
     */
    public Observable<String> fetchArticleObservable(final String wikiArticle) {
        return wikiServiceJapi.getArticleObservable(wikiArticle)
            .doOnNext(record(wikiArticle));
    }

    private Action1<String> record(String wikiArticle) {
        return article -> {
            if (record && StringUtils.isNotBlank(article)) {
                try {
                    Path path = Paths.get("./src/main/resources/mock/" + wikiArticle + ".txt");
                    if (!path.toFile().exists()) {
                        print("RECORDING: writing Article '%s' to %s", wikiArticle, path);
                        Files.write(path, article.getBytes());
                    } else {
                        print("RECORDING: mock data for Article '%s' already exists: %s", wikiArticle, path);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formated string
     */
    public Observable<String> fetchArticleObservableScheduled(final String wikiArticle) {
        return Observable.create(subscriber ->
            fetchArticleCompletableFuture(wikiArticle).whenComplete((result, error) -> {
                if (error != null) {
                    subscriber.onError(error);
                } else {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }));
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formated string
     */
    public String fetchArticle(final String wikiArticle) {
        return wikiServiceJapi.getArticle(wikiArticle);
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formatted string
     */
    public void fetchArticleCallback(final String wikiArticle, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        try {
            fetchArticleCompletableFuture(wikiArticle)
                .thenAccept(articleConsumer);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
        }
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formated string
     */
    public Future<String> fetchArticleFuture(final String wikiArticle) {
        return pool.submit(() -> wikiServiceJapi.getArticle(wikiArticle));
    }

    /**
     * @param wikiArticle name of the article to be fetched
     * @return fetches a wiki article as a media wiki formated string
     */
    public CompletableFuture<String> fetchArticleCompletableFuture(final String wikiArticle) {
        return CompletableFuture.supplyAsync(() -> wikiServiceJapi.getArticle(wikiArticle), pool);
    }

    //---------------------------------------------------------------------------------

    /**
     * @param mediaWikiText Media Wiki formated text
     * @return parsed text in structured form
     */
    public Observable<ParsedPage> parseMediaWikiTextObservable(String mediaWikiText) {
        return parser.parseObservable(mediaWikiText);
    }


    /**
     * @param mediaWikiText Media Wiki formated text
     * @return parsed text in structured form
     */
    public ParsedPage parseMediaWikiText(String mediaWikiText) {
        return parser.parse(mediaWikiText);
    }


    public Future<ParsedPage> parseMediaWikiTextFuture(String mediaWikiText) {
        return pool.submit(() -> parseMediaWikiText(mediaWikiText));
    }


    public CompletableFuture<ParsedPage> parseMediaWikiTextCompletableFuture(String mediaWikiText) {
        return CompletableFuture.supplyAsync(() -> parseMediaWikiText(mediaWikiText), pool);
    }

    public void setParser(MediaWikiTextParser parser) {
        this.parser = parser;
    }

    public void setWikiServiceJapi(WikipediaServiceJapi wikiServiceJapi) {
        this.wikiServiceJapi = wikiServiceJapi;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }
}
