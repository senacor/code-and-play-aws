package com.senacor.tecco.ilms.common.response;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created on 27.06.2016 by Dr. Michael Menzel, Senacor Technologies.
 */
public class ServiceResponseTest {

    @Test
    public void isSuccessful() throws Exception {
        assertThat(new ServiceResponse().isSuccessful(), is(true));
        assertThat(new ServiceResponse(Lists.newArrayList()).isSuccessful(), is(true));
        assertThat(new ServiceResponse(Lists.newArrayList(
                new Message("1", Severity.INFO),
                new Message("2", Severity.WARNING)))
                .isSuccessful(), is(true));
        assertThat(new ServiceResponse(Lists.newArrayList(
                new Message("1", Severity.ERROR),
                new Message("2", Severity.WARNING)))
                .isSuccessful(), is(false));
    }

    @Test
    public void testCreateServiceResponse() {

        String RESULT = "resultString";

        ServiceResponse<String> serviceResponse = new ServiceResponse<>(RESULT);

        assertEquals(RESULT, serviceResponse.getResult());
    }

    @Test
    public void testCreateWithMessage() {

        String RESULT = "resultString";

        ServiceResponse<String> serviceResponse = new ServiceResponse<>(RESULT);
        List<Message> messages = new LinkedList<>();
        messages.add(new Message.Builder()
                .code("CODE")
                .severity(Severity.WARNING)
                .description("TEST").build());

        serviceResponse.setMessages(messages);

        assertEquals(RESULT, serviceResponse.getResult());
    }

    @Test
    public void testCreateWithErrorMessage() {

        String RESULT = "resultString";

        ServiceResponse<String> serviceResponse = new ServiceResponse<>(RESULT);
        List<Message> messages = new LinkedList<>();
        messages.add(new Message.Builder()
                .code("CODE")
                .severity(Severity.ERROR)
                .description("TEST").build());

        serviceResponse.setMessages(messages);

        assertEquals(RESULT, serviceResponse.getResult());
    }

    @Test
    public void testCreateWithErrorMessageInConstructor() {

        String RESULT = "resultString";

        List<Message> messages = new LinkedList<>();
        messages.add(new Message.Builder()
                .code("CODE")
                .severity(Severity.ERROR)
                .description("TEST").build());

        ServiceResponse<String> serviceResponse = new ServiceResponse<>(messages, RESULT);

        assertEquals(RESULT, serviceResponse.getResult());
    }

    @Test
    public void testCreateWithSingleErrorMessageInConstructor() {

        String RESULT = "resultString";

        Message message = new Message.Builder()
                .code("CODE")
                .severity(Severity.ERROR)
                .description("TEST").build();

        ServiceResponse<String> serviceResponse = new ServiceResponse<>(message);
    }
}

