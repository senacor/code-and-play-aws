package com.senacor.tecco.ilms.wikiplaneparser.config;

import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andreas Keefer
 */
@Configuration
public class Config {

    @Bean
    public MediaWikiParserFactory mediaWikiParserFactory() {
        return new MediaWikiParserFactory();
    }

    @Bean
    public MediaWikiParser mediaWikiParser() {
        return mediaWikiParserFactory().createParser();
    }
}
