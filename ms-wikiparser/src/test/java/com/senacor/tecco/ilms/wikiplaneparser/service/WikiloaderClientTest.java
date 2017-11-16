package com.senacor.tecco.ilms.wikiplaneparser.service;

import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.wikiplaneparser.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles({"lokal", "firstflight"})
public class WikiloaderClientTest {

    @Autowired
    private WikiloaderClient wikiloaderClient;

    @Test
    @Ignore("only for manual testing")
    public void fetchArticle() throws Exception {
        System.out.println("fetchArticle...");

        ServiceResponse<String> res = wikiloaderClient.fetchArticle("Boeing 777").toBlocking().value();

        assertThat(res).isNotNull();
        System.out.println(res.getResult());
        assertThat(res.getMessages()).isEmpty();
        assertThat(res.getResult()).isNotEmpty();
    }

}