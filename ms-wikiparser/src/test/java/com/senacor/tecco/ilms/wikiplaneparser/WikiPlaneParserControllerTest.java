package com.senacor.tecco.ilms.wikiplaneparser;

import com.senacor.tecco.ilms.common.context.ServicesRequestContextFilter;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles({"lokal", "buildnumber"})
public class WikiPlaneParserControllerTest {

    // We specify where the generated snippets will be stored
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private RestDocumentationResultHandler document;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void plainInfoDoku() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/services/plain/info")
            .accept(MediaType.APPLICATION_JSON)
            .headers(getAuthHeader())
            .param("articleName", "Boeing 777"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(document
                .document(
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("articleName").description("wiki article name to fetch"))
                    ,PayloadDocumentation.responseFields( // We are documenting the response
                        PayloadDocumentation.fieldWithPath("messages").description("Request messages"),
                        PayloadDocumentation.fieldWithPath("result").description("parsed plane info"),
                        PayloadDocumentation.fieldWithPath("links").description("HATEOAS Links")
                    )
                ))
            ;
    }

    @Test
    public void plainInfo() throws Exception {
        //when
        ResponseEntity<ServiceResponse<String>> response = restTemplate.exchange(
            "/services/plain/info?articleName={articleName}",
            HttpMethod.GET, getDefaultRequestEntity(),
            new ParameterizedTypeReference<ServiceResponse<String>>() {
            }, "Boeing 777");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getResult(), is("1361"));
    }

    @Test
    public void wordcount() throws Exception {
        //when
        ResponseEntity<ServiceResponse<String>> response = restTemplate.exchange(
                "/services/wiki/article/{articleName}/wordcount",
                HttpMethod.GET, getDefaultRequestEntity(),
                new ParameterizedTypeReference<ServiceResponse<String>>() {
                }, "Boeing 777");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getResult(), is("12804"));
    }

    @Test
    public void rating() throws Exception {
        //when
        ResponseEntity<ServiceResponse<String>> response = restTemplate.exchange(
                "/services/wiki/article/{articleName}/rating",
                HttpMethod.GET, getDefaultRequestEntity(),
                new ParameterizedTypeReference<ServiceResponse<String>>() {
                }, "Boeing 777");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getResult(), is("2"));
    }

    private HttpEntity<Object> getDefaultRequestEntity() {
        return new HttpEntity<>(getAuthHeader());
    }

    private HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzY3MtaXNzdWVyIiwiaWF0IjoxNDcyNjQyMjY1LCJleHAiOjE5MTQ0MDU0NzQsImF1ZCI6InNlbmFjb3IuY29tIiwic3ViIjoibXNpbEBzZW5hY29yLmNvbSIsImNsaWVudF9pZCI6Im1vYmlsZSIsInVzZXJfbmFtZSI6InRlc3R1c2VyIiwidHJhY2UiOiIzN2YwNGE0OS0yMTU2LTRlODUtYTljMy1jYzI2MTU4NjQxMDkifQ.OGitxMxn7kVAybD1sGi_XrbrHSEy7ZD8JZdvKvFbPb4");
        return headers;
    }
}
