package com.senacor.tecco.ilms.wikiplaneparser.service;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class WikiFirstFlightPlaneParserServiceTest {

    private WikiFirstFlightPlaneParserService service = new WikiFirstFlightPlaneParserService();
    private FetchArticleServiceMock fetchArticleMock = new FetchArticleServiceMock();

    @Test
    public void parseBuildNumber() throws Exception {
        assertThat(getFirstFlight("Boeing 777"), is("June 12, 1994"));
    }

    @Test
    public void testMockData() throws Exception {
        assertBuildNumber("Boeing 777", "June 12, 1994");
        assertBuildNumber("Boeing 747", "February 9, 1969");
        assertBuildNumber("Boeing 737", "April 9, 1967");
        assertBuildNumber("Airbus A330", "2 November 1992");
        assertBuildNumber("Airbus A320 family", "22 February 1987");
        assertBuildNumber("Airbus_A380", null);
    }

    private void assertBuildNumber(String articleName, String expectedBuildNumber) {
        assertThat("first flight date of " + articleName, getFirstFlight(articleName), is(expectedBuildNumber));
    }

    private String getFirstFlight(String name) {
        String article = fetchArticleMock.fetchArticle(name).toBlocking().value();
        assertThat(article, notNullValue());
        return service.parse(article).toBlocking().value();
    }
}
