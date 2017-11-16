package com.senacor.tecco.ilms.common.context;

import com.senacor.tecco.ilms.common.context.demo.Demo;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContextTestApplication.class, webEnvironment = RANDOM_PORT)
public class ContextDemoControlerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void hello() throws Exception {
        //when
        ResponseEntity<ServiceResponse<List<Demo>>> response = restTemplate.exchange(path("/demo/hello/world"),
                HttpMethod.GET, getDefaultRequestEntity(), new ParameterizedTypeReference<ServiceResponse<List<Demo>>>() {
                });

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello ", response.getBody().getResult().get(0).getMsg());
        assertEquals("world", response.getBody().getResult().get(0).getName());
    }

    @Test
    public void helloSingle() throws Exception {
        //when
        ResponseEntity<Demo> response = restTemplate.exchange(path("/demo/helloSingle/world"),
                HttpMethod.GET, getDefaultRequestEntity(), new ParameterizedTypeReference<Demo>() {
                });

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello ", response.getBody().getMsg());
        assertEquals("world", response.getBody().getName());
    }

    @Test
    public void helloBlocking() throws Exception {
        //when
        ResponseEntity<Demo> response = restTemplate.exchange(path("/demo/helloBlocking/world"),
                HttpMethod.GET, getDefaultRequestEntity(), Demo.class);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello ", response.getBody().getMsg());
        assertEquals("world", response.getBody().getName());
    }

    @Test
    public void helloHystrix() throws Exception {
        //when
        ResponseEntity<Demo> response = restTemplate.exchange(path("/demo/helloHystrix/0"),
                HttpMethod.GET, getDefaultRequestEntity(), Demo.class);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Error Rate0", response.getBody().getMsg());
    }

    @Test
    public void helloHystrixFallback() throws Exception {
        //when
        ResponseEntity<Demo> response = restTemplate.exchange(path("/demo/helloHystrix/100"),
                HttpMethod.GET, getDefaultRequestEntity(), Demo.class);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fallback: Error Rate100", response.getBody().getMsg());
    }

    @Test
    public void helloHystrixObservable() throws Exception {
        //when
        ResponseEntity<ServiceResponse<List<Demo>>> response = restTemplate.exchange(path("/demo/helloHystrixObservable/0"),
                HttpMethod.GET, getDefaultRequestEntity(), new ParameterizedTypeReference<ServiceResponse<List<Demo>>>() {
                });

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Error Rate0", response.getBody().getResult().get(0).getMsg());
    }

    @Test
    public void helloHystrixObservableError() throws Exception {
        //when
        ResponseEntity<ServiceResponse<List<Demo>>> response = restTemplate.exchange(path("/demo/helloHystrixObservable/100"),
                HttpMethod.GET, getDefaultRequestEntity(), new ParameterizedTypeReference<ServiceResponse<List<Demo>>>() {
                });

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fallback: Error Rate100", response.getBody().getResult().get(0).getMsg());
    }

    @Test
    public void helloHystrixSingle() throws Exception {
        //when
        ResponseEntity<Demo> response = restTemplate.exchange(path("/demo/helloHystrixSingle/0"),
                HttpMethod.GET, getDefaultRequestEntity(), Demo.class);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Error Rate0", response.getBody().getMsg());
    }

    private String path(String context) {
        return "/services" + context;
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