package com.senacor.tecco.ilms.wikiloader.service;

import com.google.common.collect.Sets;
import com.senacor.tecco.ilms.wikiloader.config.WikiConfig;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WikiConfig.class, WikiServiceTest.class})
@ComponentScan("com.senacor.tecco.ilms.wikiloader.service")
@ActiveProfiles({"lokal", "mock"})
public class WikiServiceTest {

    @Autowired
    private WikiService wikiService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        properties.setProperty("wikipedia.language", "de");

        pspc.setProperties(properties);
        return pspc;
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Sets.newHashSet(
            new ConcurrentMapCache("wiki.article", true)
        ));
        return simpleCacheManager;
    }

//    @Bean
//    @Primary
//    WikipediaServiceJapi wikipediaServiceJapiMock(){
//        return new WikipediaServiceJapiMock(1);
//    }

    @Test
    public void fetchArticleObservable() throws Exception {
        String article = wikiService.fetchArticleObservable("42").toBlocking().first();
        System.out.println(article);
        assertNotNull(article);
    }

    @Test
    public void testFetchArticle() throws Exception {
        String article = wikiService.fetchArticle("42");
        System.out.println(article);
        assertNotNull(article);
    }

    @Test
    public void testParseMediaWikiText() throws Exception {
        ParsedPage parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==").toBlocking().first();
        assertNotNull(parsedPage);
        assertEquals(1, parsedPage.getSections().size());
        assertEquals("Weblinks", parsedPage.getSections().iterator().next().getText());
    }

    @Test
    public void testParseMediaWikiTextWithLink() throws Exception {
        ParsedPage parsedPage = getParseMediaWikiTextWithLink();
        assertNotNull(parsedPage);
        assertEquals(1, parsedPage.getSections().size());
        assertEquals("Weblinks 42", parsedPage.getSections().iterator().next().getText());
        assertEquals(1, parsedPage.getLinks().size());

    }

    private ParsedPage getParseMediaWikiTextWithLink() throws Exception {
        ParsedPage parsedPage = wikiService.parseMediaWikiTextObservable("== Weblinks ==\n [[42]]").toBlocking().first();
        return parsedPage;
    }

}
