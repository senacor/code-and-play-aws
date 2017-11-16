package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class FetchArticleServiceMockTest {

    private FetchArticleServiceMock fetchArticleMock = new FetchArticleServiceMock();

    @Test
    public void readArticle() throws Exception {
        String article = fetchArticleMock.fetchArticle("Boeing 777").toBlocking().value();
        assertThat(article, notNullValue());
    }

}
