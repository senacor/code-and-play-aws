package com.senacor.tecco.ilms.wikiloader.service.integration;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Andreas Keefer
 */
public class WikipediaServiceJapiMockTest {

    @Test
    public void getPageContent() throws Exception {
        WikipediaServiceJapiMock wikipediaServiceJapiMock = new WikipediaServiceJapiMock(1000);
        wikipediaServiceJapiMock.setDelayUtil(new DelayUtil());
        wikipediaServiceJapiMock.getPageContent("42");
    }

}