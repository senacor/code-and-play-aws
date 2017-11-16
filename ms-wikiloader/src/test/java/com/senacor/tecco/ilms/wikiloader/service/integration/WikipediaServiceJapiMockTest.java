package com.senacor.tecco.ilms.wikiloader.service.integration;

import com.senacor.tecco.ilms.common.utils.DelayUtil;
import org.junit.Test;

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