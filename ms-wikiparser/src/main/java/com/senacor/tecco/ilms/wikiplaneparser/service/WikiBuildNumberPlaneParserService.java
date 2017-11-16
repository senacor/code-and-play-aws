package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.apache.commons.lang3.Validate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rx.Single;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * parses the Buildnumber (how many Planes are build)
 *
 * @author Andreas Keefer
 */
@Component
@Profile("buildnumber")
public class WikiBuildNumberPlaneParserService implements WikiPlaneParserService {

    public Single<String> parse(String planeArticle) {
        return Single.create(singleSubscriber -> {
            try {
                Validate.notBlank(planeArticle, "Wiki Article must not be empty");
                Pattern pattern = Pattern.compile("number built ?= ?([\\d,]*)");
                Matcher matcher = pattern.matcher(planeArticle);
                if (matcher.find()) {
                    singleSubscriber.onSuccess(matcher.group(1)
                        .replace(",", "")
                        .replace(".", ""));
                }
                singleSubscriber.onSuccess(null);
            } catch (Exception e) {
                singleSubscriber.onError(e);
            }
        });
    }
}
