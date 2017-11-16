package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rx.Single;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * parses the FirstFlight Date
 *
 * @author Andreas Keefer
 */
@Component
@Profile("firstflight")
public class WikiFirstFlightPlaneParserService implements WikiPlaneParserService {

    public Single<String> parse(String planeArticle) {
        return Single.create(singleSubscriber -> {
            try {
                Pattern pattern = Pattern.compile("first flight ?= ?([0-9A-Za-z, ]*)"); //first flight= June 12, 1994
                Matcher matcher = pattern.matcher(planeArticle);
                if (matcher.find()) {
                    singleSubscriber.onSuccess(matcher.group(1));
                }
                singleSubscriber.onSuccess(null);
            } catch (Exception e) {
                singleSubscriber.onError(e);
            }
        });
    }
}
