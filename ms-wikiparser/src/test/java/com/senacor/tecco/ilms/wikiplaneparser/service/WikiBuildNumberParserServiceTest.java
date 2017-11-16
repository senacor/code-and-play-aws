package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class WikiBuildNumberParserServiceTest {

    private WikiBuildNumberPlaneParserService service = new WikiBuildNumberPlaneParserService();
    private FetchArticleServiceMock fetchArticleMock = new FetchArticleServiceMock();

    @Test
    public void parseBuildNumber() throws Exception {
        assertThat(getBuildNumber("Boeing 777"), is("1361"));
    }

    @Test
    public void testMockData() throws Exception {
        assertBuildNumber("Boeing 777", "1361");
        assertBuildNumber("Boeing 747", "1519");
        assertBuildNumber("Boeing 737", "8845");
        assertBuildNumber("Airbus A330", "1257");
        assertBuildNumber("Airbus A320 family", "7061");
        assertBuildNumber("Airbus_A380", null);
    }

    private void assertBuildNumber(String articleName, String expectedBuildNumber) {
        assertThat("buildnumber of " + articleName, getBuildNumber(articleName), is(expectedBuildNumber));
    }

    private String getBuildNumber(String name) {
        String article = fetchArticleMock.fetchArticle(name).toBlocking().value();
        assertThat(article, notNullValue());
        return service.parse(article).toBlocking().value();
    }
}
