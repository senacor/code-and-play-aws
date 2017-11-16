package com.senacor.tecco.ilms.wikiloader.service.integration;

import com.google.common.collect.Sets;
import com.senacor.tecco.ilms.wikiloader.config.WikiConfig;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WikiConfig.class, MediaWikiTextParserTest.class})
@ComponentScan("com.senacor.tecco.ilms.wikiloader.service")
@ActiveProfiles("lokal")
public class MediaWikiTextParserTest {

    @Autowired
    private MediaWikiTextParser mediaWikiTextParser;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Sets.newHashSet(
            new ConcurrentMapCache("wiki.article", true)
        ));
        return simpleCacheManager;
    }

    @Test
    public void demonstrateUsage() {
        ParsedPage parsedPage = mediaWikiTextParser.parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                "== Religion ==\n" +
                "\n" +
                "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                "== Weblinks ==\n" +
                "\n" +
                "{{commonscat|42}}");
        for (Link link : parsedPage.getLinks()) {
            System.out.println("link=" + link.getTarget());
        }
        for (Section section : parsedPage.getSections()) {
            System.out.println("Section: " + section.getTitle());
            for (Link link : section.getLinks(Link.type.INTERNAL)) {
                System.out.println(" sectionLink=" + link.getTarget());
            }
        }
    }
}
