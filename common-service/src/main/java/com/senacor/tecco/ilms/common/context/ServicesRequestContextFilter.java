package com.senacor.tecco.ilms.common.context;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.senacor.tecco.ilms.common.context.hystrix.ContextHystrixRequestVariable;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

/**
 * The Class ServicesRequestContextFilter.
 */
public class ServicesRequestContextFilter extends OncePerRequestFilter {
    /**
     * The Constant AUTHORIZATION_SCHEMA.
     */
    private static final String AUTHORIZATION_SCHEMA = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || jwtToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header not found. No valid JWT token found in request.");
            return;
        }

        if (!jwtToken.startsWith(AUTHORIZATION_SCHEMA)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header doesn't start with 'Bearer'");
            return;
        }
        final ServicesRequestContextHolder context;
        try {
            JWT jwt = JWTParser.parse(jwtToken.substring(AUTHORIZATION_SCHEMA.length()).trim());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            context = ServicesRequestContextHolder.newBuilder()
                    .withClientID(claims.getStringClaim("client_id"))
                    .withUserName(claims.getStringClaim("user_name"))
                    //Authorities mit Rechten wieder aktivieren, um Authoisierung mit Spring-Security zu nutzen
                    //.withAuthorities(claims.getStringListClaim("authorities"))
                    .withRequestID(UUID.randomUUID().toString())
                    .withSessionID(null == claims.getStringClaim("trace")
                            ? UUID.randomUUID().toString()
                            : claims.getStringClaim("trace"))
                    .build();
            ServicesRequestContextHolder.set(context);
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT couldn't be parsed: " + e.getMessage());
            return;
        }

        // Enable Hystrix requestContext
        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        // set context in Hystrix RequestContextVariable
        ContextHystrixRequestVariable.getInstance().set(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            hystrixRequestContext.shutdown();
            ServicesRequestContextHolder.clear();
        }
    }

    @Override
    public void destroy() {

    }
}
