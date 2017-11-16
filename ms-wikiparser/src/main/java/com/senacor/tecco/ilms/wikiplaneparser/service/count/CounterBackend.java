package com.senacor.tecco.ilms.wikiplaneparser.service.count;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

/**
 * @author Andreas Keefer
 */
public interface CounterBackend {
    int countWords(ParsedPage parsedPage);
}
