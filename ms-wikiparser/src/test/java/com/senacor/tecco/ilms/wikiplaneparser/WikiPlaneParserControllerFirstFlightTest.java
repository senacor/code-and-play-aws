package com.senacor.tecco.ilms.wikiplaneparser;

import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles({"lokal", "firstflight"})
public class WikiPlaneParserControllerFirstFlightTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        assertThat(response.getBody().getResult(), is("June 12, 1994"));
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
