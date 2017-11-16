package com.senacor.tecco.ilms.common.exception;

import com.senacor.tecco.ilms.common.response.Message;
import com.senacor.tecco.ilms.common.response.ServiceResponse;
import com.senacor.tecco.ilms.common.response.Severity;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Andreas Keefer
 */
public class ApplicationExceptionTest {

    @Test
    public void throwOnNotSuccess_OK() throws Exception {
        ApplicationException.throwOnNotSuccess(ResponseEntity.ok(new ServiceResponse<>("test")));
    }

    @Test
    public void throwOnNotSuccess_Error() throws Exception {
        try {
            ApplicationException.throwOnNotSuccess(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServiceResponse<>("test")));
            fail("ApplicationException expected");
        } catch (ApplicationException e) {
            assertThat(e.getResponseStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR));
            assertThat(e.getMessages(), empty());
        }
    }

    @Test
    public void throwOnNotSuccess_ErrorWithMessage() throws Exception {
        Message msg = new Message("code", Severity.ERROR);
        try {
            ApplicationException.throwOnNotSuccess(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServiceResponse<>(msg)));
            fail("ApplicationException expected");
        } catch (ApplicationException e) {
            assertThat(e.getResponseStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR));
            assertThat(e.getMessages(), contains(msg));
        }
    }
}