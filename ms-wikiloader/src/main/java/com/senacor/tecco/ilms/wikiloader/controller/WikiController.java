package com.senacor.tecco.ilms.wikiloader.controller;

import com.senacor.tecco.ilms.common.exception.ApplicationException;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.wikiloader.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;


@RestController
@RequestMapping(value = "/services/wiki", produces = MediaType.APPLICATION_JSON)
public class WikiController {

    @Autowired
    WikiService wikiService;

    /**
     * GET fetchArticle
     *
     * @param articleName article name
     * @return article in media wiki format
     */
    @GetMapping("fetchArticle")
    public HttpEntity<ServiceResponse<String>> fetchArticle(@RequestParam("articleName") String articleName)
            throws ApplicationException {
        return getArticle(articleName);
    }

    /**
     * GET article
     *
     * @param articleName article name
     * @return article in media wiki format
     */
    @GetMapping("article/{articleName}")
    public HttpEntity<ServiceResponse<String>> getArticle(@PathVariable("articleName") String articleName)
            throws ApplicationException {
        String res = wikiService.fetchArticle(articleName);
        ServiceResponse<String> serviceResponse = new ServiceResponse<>(res);
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }
}
