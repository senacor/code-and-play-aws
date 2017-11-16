package com.senacor.tecco.ilms.wikiplaneparser.service;

import rx.Single;

/**
 * @author Andreas Keefer
 */
public interface WikiPlaneParserService {

    /**
     * parsed data from a Plane Wikipedia Article
     *
     * @param planeArticle Wikipedia Plane Article
     * @return parsed value
     */
    Single<String> parse(String planeArticle);
}
