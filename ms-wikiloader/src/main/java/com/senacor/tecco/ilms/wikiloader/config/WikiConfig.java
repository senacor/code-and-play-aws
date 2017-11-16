package com.senacor.tecco.ilms.wikiloader.config;

import com.bitplan.mediawiki.japi.Mediawiki;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andreas Keefer
 */
@Configuration
@EnableCaching
public class WikiConfig {

    @Value("${wikipedia.language}")
    private String wikipediaLanguage;

    @Bean
    public MediaWikiParserFactory mediaWikiParserFactory() {
        return new MediaWikiParserFactory();
    }

    @Bean
    public MediaWikiParser mediaWikiParser() {
        return mediaWikiParserFactory().createParser();
    }

    @Bean
    public Mediawiki mediawiki() throws Exception {
        return new Mediawiki("https://" + wikipediaLanguage + ".wikipedia.org");
    }
}
