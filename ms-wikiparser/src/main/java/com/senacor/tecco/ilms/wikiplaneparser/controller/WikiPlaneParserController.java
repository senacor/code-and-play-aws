package com.senacor.tecco.ilms.wikiplaneparser.controller;

import com.senacor.tecco.ilms.common.exception.ApplicationException;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.wikiplaneparser.service.FetchArticleService;
import com.senacor.tecco.ilms.wikiplaneparser.service.MediaWikiTextParser;
import com.senacor.tecco.ilms.wikiplaneparser.service.WikiPlaneParserService;
import com.senacor.tecco.ilms.wikiplaneparser.service.count.CounterBackend;
import com.senacor.tecco.ilms.wikiplaneparser.service.rating.RatingBackend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/services")
public class WikiPlaneParserController {

    @Autowired
    private WikiPlaneParserService wikiPlaneParserService;

    @Autowired
    private FetchArticleService fetchArticleService;

    @Autowired
    private MediaWikiTextParser parser;

    @Autowired
    private CounterBackend counterBackend;

    @Autowired
    private RatingBackend ratingBackend;

    /**
     * GET fetchArticle
     *
     * @param articleName article name
     * @return article in media wiki format
     */
    @RequestMapping(value = "/plain/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponse<String> fetchArticle(@RequestParam("articleName") String articleName)
            throws ApplicationException {
        // TODO(ak): wenn das Interface hier Single<ServiceResponse<String>> verwendet wird, gibt es 2 probleme:
        // TODO(ak): - es wird automatisch ein HystrixCommand darum gewrapped, der vor dem Context filter ist und dann "HystrixRequestContext.initializeContext()" noch nicht gerufen wurde
        // TODO(ak): - die restdocs doku funktioniert dann nicht, der body ist leer
        return fetchArticleService.fetchArticle(articleName)
                .flatMap(article -> wikiPlaneParserService.parse(article))
                .map(ServiceResponse::new)
                .toBlocking().value();
    }

    @RequestMapping(value = "wiki/article/{articleName}/wordcount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponse<Integer> countWords(@PathVariable("articleName") String articleName) {
        return fetchArticleService.fetchArticle(articleName)
                .map(article -> parser.parse(article))
                .map(parsedPage -> counterBackend.countWords(parsedPage))
                .map(ServiceResponse::new)
                .toBlocking().value();
    }

    @RequestMapping(value = "wiki/article/{articleName}/rating", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponse<Integer> rating(@PathVariable("articleName") String articleName) {
        return fetchArticleService.fetchArticle(articleName)
                .map(article -> parser.parse(article))
                .map(parsedPage -> ratingBackend.rate(parsedPage))
                .map(ServiceResponse::new)
                .toBlocking().value();
    }
}
