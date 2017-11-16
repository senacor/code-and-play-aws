package com.senacor.tecco.ilms.common.exception;

        import com.senacor.tecco.ilms.common.response.ServiceResponse;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.boot.autoconfigure.web.ErrorController;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.ResponseBody;

        import javax.servlet.http.HttpServletRequest;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.util.Arrays;
        import java.util.Enumeration;
        import java.util.List;

/**
 * Created on 06.07.2016 by Dr. Michael Menzel, Senacor Technologies.
 */
@Controller
public class GlobalErrorController implements ErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalErrorController.class);

    /**
     * Http header in this list are excluded from logging.
     */
    private static final List<String> HEADER_BLACKLIST = Arrays.asList("Authorization");

    @Value("${server.error.path:/error}")
    private String errorPath;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping(value = "${server.error.path:/error}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<ServiceResponse<Void>> error(HttpServletRequest request){
        LOG.debug("Global Error Controller triggered!");

        Object exception = request.getAttribute("javax.servlet.error.exception");
        ResponseEntity<ServiceResponse<Void>> response;

        //TODO: Wird das in 1.4 besser?

        if (exception instanceof ApplicationException) {
            response = globalExceptionHandler.handleApplicationException((ApplicationException) exception);

        } else if (exception instanceof RuntimeException) {
            response = globalExceptionHandler.handleRuntimeException((RuntimeException) exception);

        } else {
            Object errorMessage = request.getAttribute("javax.servlet.error.message");
            Object errorCode = request.getAttribute("javax.servlet.error.status_code");
            logErrorRequest(request);

            if (errorMessage != null && errorMessage instanceof String) {
                response = ErrorResponseFactory.createErrorResponseEntity((Integer) errorCode, (String) errorMessage);
            } else {
                response = ErrorResponseFactory.createErrorResponseEntity();
            }
            LOG.error(response.getBody().getMessages().get(0).toString());
        }

        return response;
    }

    //TODO: Move this and getBody to its own class
    private void logErrorRequest(HttpServletRequest request) {
        Enumeration<String> attrNames = request.getAttributeNames();
        LOG.debug("Error Response Attrributes");
        while(attrNames.hasMoreElements()) {
            String attrName = attrNames.nextElement();
            if(HEADER_BLACKLIST.contains(attrName)) {
                continue;
            }
            LOG.debug(attrName + ":" + request.getAttribute(attrName).toString());
        }
        LOG.debug("Error Request Body: " + getBody(request));
    }

    public static String getBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder("");
        try {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));) {
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException | NullPointerException ex) {
            LOG.error("Error in GlobalErrorController while reading the request body.", ex);
        }
        return stringBuilder.toString();
    }

}
