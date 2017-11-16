package com.senacor.tecco.ilms.wikiplaneparser.service;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import org.springframework.stereotype.Component;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
@Component
public class MediaWikiTextParser {

    private final MediaWikiParser parser;

    public MediaWikiTextParser(MediaWikiParser parser) {
        this.parser = parser;
    }

    public ParsedPage parse(String mediaWikiText) {
        final long start = System.currentTimeMillis();
        ParsedPage res = parser.parse(mediaWikiText);
        System.out.println(ReactiveUtil.getThreadId() + "profiling parse("
            //+ res.getName()
            //+ mediaWikiText
            + "): " + (System.currentTimeMillis() - start) + "ms");
        return res;
    }

    public Observable<ParsedPage> parseObservable(final String mediaWikiText) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(parse(mediaWikiText));
                subscriber.onCompleted();
            } catch (RuntimeException e) {
                subscriber.onError(e);
            }
        });
    }
}
