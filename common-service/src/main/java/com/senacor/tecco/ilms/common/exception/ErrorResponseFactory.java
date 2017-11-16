package com.senacor.tecco.ilms.common.exception;

import com.senacor.tecco.ilms.common.response.Message;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.common.response.Severity;
import org.apache.http.client.cache.HeaderConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Created on 06.07.2016 by Dr. Michael Menzel, Senacor Technologies.
 */
class ErrorResponseFactory {

    /**
     * creates ResponseEntity from an ApplicationException
     *
     * @param exception ApplicationException with response status and messages
     * @return response entity
     */
    static ResponseEntity<ServiceResponse<Void>> convertApplicationExceptionToResponseEntity(ApplicationException exception) {
        ServiceResponse<Void> serviceResponse = new ServiceResponse<>(exception.getMessages());
        return ResponseEntity
                .status(exception.getResponseStatus())
                .body(serviceResponse);
    }

    /**
     * creates ResponseEntity from an Exception
     *
     * @param exception unknown exception
     * @return response entity
     */
    static ResponseEntity<ServiceResponse<Void>> convertExceptionToResponseEntity(Exception exception) {

        ServiceResponse<Void> serviceResponse = ErrorResponseFactory.createInternalServerErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .headers(createHttpHeaders())
                .body(serviceResponse);
    }

    /**
     * creates generic error response entity
     *
     * @return response entity
     */
    static ResponseEntity<ServiceResponse<Void>> createErrorResponseEntity() {
        return createErrorResponseEntity(null);
    }

    /**
     * creates generic error response entity
     *
     * @return response entity
     */
    static ResponseEntity<ServiceResponse<Void>> createErrorResponseEntity(String msg) {

        ServiceResponse<Void> serviceResponse = ErrorResponseFactory.createInternalServerErrorResponse(msg);

        return ResponseEntity
                .status(org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .headers(createHttpHeaders())
                .body(serviceResponse);
    }

    /**
     * creates error response entity with code and msh
     *
     * @return response entity
     */
    static ResponseEntity<ServiceResponse<Void>> createErrorResponseEntity(Integer code, String msg) {

        if (code == null) {
            return createErrorResponseEntity(msg);
        }

        ServiceResponse<Void> serviceResponse = createServletErrorResponse(msg);

        return ResponseEntity
                .status(code)
                .headers(createHttpHeaders())
                .body(serviceResponse);
    }


    /**
     * creates an Internal Server Error Response
     *
     * @return ServiceResponse
     */
    static ServiceResponse<Void> createInternalServerErrorResponse(String cause) {
        Message errorMessage = new Message.Builder()
                .code("Internal_Server_Error")
                .severity(Severity.ERROR)
                .description("Internal Server Error").build();

        if (cause != null) {
            errorMessage.addParameter("cause", cause);
        }

        return new ServiceResponse<Void>(errorMessage);
    }

    /**
     * creates an servlet error Response
     *
     * @return ServiceResponse
     */
    static ServiceResponse<Void> createServletErrorResponse(String cause) {
        Message errorMessage = new Message.Builder()
                .code("Servlet_Error")
                .severity(Severity.ERROR)
                .description(cause).build();

        return new ServiceResponse<Void>(errorMessage);
    }

    static HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(HeaderConstants.CACHE_CONTROL_NO_STORE);
        headers.setPragma(HeaderConstants.CACHE_CONTROL_NO_CACHE);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }
}