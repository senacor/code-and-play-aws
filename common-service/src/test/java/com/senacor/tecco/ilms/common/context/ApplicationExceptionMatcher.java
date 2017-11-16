package com.senacor.tecco.ilms.common.context;

import com.senacor.tecco.ilms.common.exception.ApplicationException;
import com.senacor.tecco.ilms.common.response.Message;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.http.HttpStatus;

/**
 * Created on 30.06.2016 by Dr. Michael Menzel, Senacor Technologies.
 */


/**
 * Created on 30.06.2016 by Dr. Michael Menzel, Senacor Technologies.
 * Matcher to test ApplicationExceptions
 */
public class ApplicationExceptionMatcher extends BaseMatcher<Throwable> {
    private String expectedMessageCode;
    private HttpStatus expectedHttpStatus;
    private String errorDescription;

    public ApplicationExceptionMatcher(String code, HttpStatus httpStatus) {
        this.expectedMessageCode = code;
        this.expectedHttpStatus = httpStatus;
    }

    //TODO: Die Test funktionieren nicht mehr, da keine Application-Exception mehr geworfen wird...


    @Override
    public boolean matches(Object object) {
        //check Exception type
        if (!ApplicationException.class.isAssignableFrom(object.getClass())) {
            errorDescription = "ApplicationException expected instead " + object.getClass().getName();
            return false;
        }

        ApplicationException exception = (ApplicationException)object;

        //check Http Code
        if(!expectedHttpStatus.equals(exception.getResponseStatus())){
            errorDescription = "ApplicationException with wrong HTTP status. Expected: " + expectedHttpStatus + ", Found: " + exception.getResponseStatus();
            return false;
        }

        //check message code
        for (Message message: exception.getMessages()) {
            if(message.getCode().equals(expectedMessageCode)){
                return true;
            }
        }

        errorDescription = "ApplicationException with wrong message code. Expected: " + expectedMessageCode + ", Found: " + getMessageCodeList(exception);

        return false;
    }

    /**
     * extracts all message codes and appends it to an list
     * @param exception
     * @return
     */
    private String getMessageCodeList(ApplicationException exception) {
        String messageCodes = "";
        for (Message message: exception.getMessages()) {
            if(messageCodes.length() > 0){
                messageCodes += " ,";
            }
            messageCodes += message.getCode();
        }
        return messageCodes;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(errorDescription);
    }
}