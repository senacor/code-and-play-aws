package com.senacor.tecco.ilms.wikiloader.controller;

import com.senacor.tecco.ilms.common.exception.ApplicationException;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.wikiloader.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;


@RestController
@RequestMapping("/services/wiki")
public class WikiController {

    @Autowired
    WikiService wikiService;

    /**
     * GET fetchArticle
     *
     * @param articleName article name
     * @return article in media wiki format
     */
    @RequestMapping(value = "fetchArticle", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public HttpEntity<ServiceResponse<String>> fetchArticle(@RequestParam(value = "articleName") String articleName)
        throws ApplicationException {
        String res = wikiService.fetchArticle(articleName);
        ServiceResponse<String> serviceResponse = new ServiceResponse<>(res);
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }
}
