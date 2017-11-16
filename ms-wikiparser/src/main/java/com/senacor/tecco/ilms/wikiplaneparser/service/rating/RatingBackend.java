package com.senacor.tecco.ilms.wikiplaneparser.service.rating;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

/**
 * @author Andreas Keefer
 */
public interface RatingBackend {
    int rate(ParsedPage parsedPage);
}
