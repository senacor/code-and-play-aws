package com.senacor.tecco.ilms.common.exception;

import com.google.common.collect.Iterables;
import com.senacor.tecco.ilms.common.response.Message;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Application Exceptions convey messages and the HTTP response status code that is returned to the client
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -4409163052757675208L;

    private List<Message> messages = new LinkedList<>();
    private HttpStatus responseStatus;

    protected ApplicationException(String exceptionText) {
        super(exceptionText);
        this.messages = new LinkedList<>();
    }

    protected ApplicationException(String exceptionText, Throwable cause) {
        super(exceptionText, cause);
        this.messages = new LinkedList<>();
    }

    public ApplicationException(HttpStatus httpStatus) {
        this(httpStatus, (Throwable) null);
    }

    public ApplicationException(HttpStatus httpStatus, Throwable cause) {
        super(httpStatus.getReasonPhrase(), cause);
        this.responseStatus = httpStatus;
        this.messages = new LinkedList<>();
    }

    public ApplicationException(Message... messages) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, messages);
    }

    public ApplicationException(Throwable cause, Message... messages) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, cause, messages);
    }

    public ApplicationException(HttpStatus httpStatus, Message... messages) {
        this(httpStatus, (Throwable) null, messages);
    }

    public ApplicationException(HttpStatus httpStatus, Iterable<Message> messages) {
        this(httpStatus, (Throwable) null, Iterables.toArray(messages, Message.class));
    }

    public ApplicationException(HttpStatus httpStatus, Throwable cause, Message... messages) {
        super(messages.length > 0 ? messages[0].getCodePlusSeverity() : "", cause);
        this.responseStatus = httpStatus;
        for (Message message : messages) {
            this.messages.add(message);
        }
    }

    public static <T> void throwOnNotSuccess(ResponseEntity<ServiceResponse<T>> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            if (null != response.getBody() && null != response.getBody().getMessages()) {
                throw new ApplicationException(response.getStatusCode(), response.getBody().getMessages());
            }
            throw new ApplicationException(response.getStatusCode());
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }
}