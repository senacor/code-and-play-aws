package com.senacor.tecco.ilms.common.exception;

import com.senacor.tecco.ilms.common.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handling of unchecked Exceptions like NullPointerException, IndexOutOfBoundsException
     */
    @ExceptionHandler({ RuntimeException.class })
    protected ResponseEntity<ServiceResponse<Void>> handleRuntimeException(RuntimeException exception) {
        LOG.debug("Global Exception Handler triggered!");
        LOG.error(exception.toString(), exception);
        //TODO: hier auf Annotation prüfen
        return ErrorResponseFactory.convertExceptionToResponseEntity(exception);
    }

    /**
     * Handling of checked Exceptions extending ApplicationException
     */
    @ExceptionHandler({ ApplicationException.class })
    protected ResponseEntity<ServiceResponse<Void>> handleApplicationException(ApplicationException exception) {
        LOG.debug("Global Exception Handler triggered for Application Exception!");
        HttpStatus status = exception.getResponseStatus();
        if (status==null || status.is5xxServerError()) {
            LOG.error(exception.toString(), exception);
        } else {
            LOG.warn(exception.toString());
        }
        return ErrorResponseFactory.convertApplicationExceptionToResponseEntity(exception);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getObjectName().equals("fromDate") || binder.getObjectName().equals("toDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
        }
    }

}

