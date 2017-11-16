package com.senacor.tecco.ilms.wikiplaneparser.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * @author Andreas Keefer
 */
public class RequestContextUtil {

    public static String getRequestJWT() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String jwtToken = request.getHeader("Authorization");
        notBlank(jwtToken, "no JWT-Token in RequestContext");
        return jwtToken;
    }
}
