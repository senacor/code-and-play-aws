package com.senacor.tecco.ilms.wikiloader.service.integration;

import com.google.common.collect.Sets;
import com.senacor.tecco.ilms.wikiloader.config.WikiConfig;
import org.junit.Assert;
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

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WikiConfig.class, WikipediaServiceJapiImplTest.class})
@ComponentScan("com.senacor.tecco.ilms.wikiloader.service")
@ActiveProfiles({"lokal"})
public class WikipediaServiceJapiImplTest {

    @Autowired
    private WikipediaServiceJapi wikipediaService;

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

    @Test
    public void demonstrate() {
        String article = wikipediaService.getArticle("42");
        System.out.println("article=" + article);
        Assert.assertThat(article, not(isEmptyOrNullString()));
    }

}
