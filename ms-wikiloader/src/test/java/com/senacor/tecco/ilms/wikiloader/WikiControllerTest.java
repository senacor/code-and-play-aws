package com.senacor.tecco.ilms.wikiloader;

/**
 * Created by asiddiqui on 30/03/16.
 * <p>
 * This test class comprises of tests for the UserController
 * <p>
 * These tests are used to generate the code snippets for Spring REST docs document.
 */


import com.senacor.tecco.ilms.common.context.ServicesRequestContextFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("lokal")
public class WikiControllerTest {

    // We specify where the generated snippets will be stored
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private RestDocumentationResultHandler document;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.document = MockMvcRestDocumentation
            //  {method-name} => The generated snippets will be kept under a folder with the test
            // method's name i.e. the folder name will be 'get-user' for the getUser method below,
            // the generated snippet location is later specified in the .asciidoc file
            .document("{method-name}",
                // pretty print json or xml
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .addFilters(new ServicesRequestContextFilter())
            .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
                .uris()
                // Specify service URI so that http requests can be documented correctly
                .withScheme("http")
                .withHost("localhost")
                .withPort(8080))
            .build();
    }

    @Test
    public void fetchArticle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/services/wiki/fetchArticle")
            .accept(MediaType.APPLICATION_JSON)
            .headers(getAuthHeader())
            .param("articleName", "42"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(document
                .snippets(
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("articleName").description("wiki article name to fetch")),
                    PayloadDocumentation.responseFields( // We are documenting the response
                        PayloadDocumentation.fieldWithPath("messages").description("Request messages"),
                        PayloadDocumentation.fieldWithPath("result").description("Wiki article in media wiki format"),
                        PayloadDocumentation.fieldWithPath("links").description("HATEOAS Links")
                    )));
    }

    private HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzY3MtaXNzdWVyIiwiaWF0IjoxNDcyNjQyMjY1LCJleHAiOjE5MTQ0MDU0NzQsImF1ZCI6InNlbmFjb3IuY29tIiwic3ViIjoibXNpbEBzZW5hY29yLmNvbSIsImNsaWVudF9pZCI6Im1vYmlsZSIsInVzZXJfbmFtZSI6InRlc3R1c2VyIiwidHJhY2UiOiIzN2YwNGE0OS0yMTU2LTRlODUtYTljMy1jYzI2MTU4NjQxMDkifQ.OGitxMxn7kVAybD1sGi_XrbrHSEy7ZD8JZdvKvFbPb4");
        return headers;
    }
}
