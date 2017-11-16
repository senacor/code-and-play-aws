package com.senacor.tecco.ilms.common.context;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ServicesRequestContextFilterTest {


    @Test
    public void thatContextServiceIsSetFromToken() throws Exception {

        // create the objects to be mocked
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        doAnswer(invocation -> {
            ServicesRequestContextHolder context = ServicesRequestContextHolder.get();
            assertEquals("mobile", context.getClientID());
            assertEquals("testuser", context.getUserName());
            assertEquals("37f04a49-2156-4e85-a9c3-cc2615864109", context.getSessionID());
            assertThat(context.getRequestID(), notNullValue());
            return null;
        }).when(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzY3MtaXNzdWVyIiwiaWF0IjoxNDcyNjQyMjY1LCJleHAiOjE5MTQ0MDU0NzQsImF1ZCI6InNlbmFjb3IuY29tIiwic3ViIjoibXNpbEBzZW5hY29yLmNvbSIsImNsaWVudF9pZCI6Im1vYmlsZSIsInVzZXJfbmFtZSI6InRlc3R1c2VyIiwidHJhY2UiOiIzN2YwNGE0OS0yMTU2LTRlODUtYTljMy1jYzI2MTU4NjQxMDkifQ.OGitxMxn7kVAybD1sGi_XrbrHSEy7ZD8JZdvKvFbPb4");

        ServicesRequestContextFilter contextFilter = new ServicesRequestContextFilter();
        contextFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        ServicesRequestContextHolder context = ServicesRequestContextHolder.get();
        assertThat(context.getClientID(), nullValue());
        assertThat(context.getUserName(), nullValue());
        assertThat(context.getSessionID(), nullValue());
        assertThat(context.getRequestID(), nullValue());

        //Asserts wieder aktivieren, wenn Rechteprüfung mit Spring-Security genutzt werden soll
        //assertEquals("ROLE_LPRA", context.getAuthorities().get(0));
        //assertEquals("ROLE_KDDV", context.getAuthorities().get(1));

        // verify if a sendRedirect() was performed with the expected value
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void thatAuthorizationHeaderIsChecked() throws Exception {

        ExpectedException thrown = ExpectedException.none();

        // create the objects to be mocked
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("");

        thrown.expect(new ApplicationExceptionMatcher("AUTHORIZATION_HEADER_MISSING", HttpStatus.UNAUTHORIZED));

        ServicesRequestContextFilter contextFilter = new ServicesRequestContextFilter();
        contextFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    @Test
    public void thatExceptionIsReturnedOnMissingToken() throws Exception {

        ExpectedException thrown = ExpectedException.none();

        // create the objects to be mocked
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("fdsklasrz7oq832z5fhas");

        thrown.expect(new ApplicationExceptionMatcher("INVALID_JWT_TOKEN", HttpStatus.BAD_REQUEST));

        ServicesRequestContextFilter contextFilter = new ServicesRequestContextFilter();
        contextFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

    }
}
