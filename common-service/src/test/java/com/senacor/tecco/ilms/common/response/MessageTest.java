package com.senacor.tecco.ilms.common.response;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created on 27.06.2016 by Dr. Michael Menzel, Senacor Technologies.
 */
public class MessageTest {

        @Test
        public void testCreateMessageWithBuilder() {
                String   CODE = "Code1";
                Severity SERVERITY = Severity.ERROR;
                String   MESSAGE = "Message1";

                Message newMessage = new Message.Builder()
                        .code(CODE)
                        .severity(Severity.ERROR)
                        .description(MESSAGE).build();

                assertEquals( CODE, newMessage.getCode());
                assertEquals( SERVERITY, newMessage.getSeverity());
                assertEquals( MESSAGE, newMessage.getDescription());
        }

        @Test
        public void testCreateMessageWithConstructor() {
                String   CODE = "Code1";
                Severity SERVERITY = Severity.ERROR;
                String   MESSAGE = "Message1";

                Message newMessage = new Message(CODE, Severity.ERROR, MESSAGE);

                assertEquals( CODE, newMessage.getCode());
                assertEquals( SERVERITY, newMessage.getSeverity());
                assertEquals( MESSAGE, newMessage.getDescription());
        }

        @Test
        public void testCreateMessageWithConstructorAndParameter() {
                String   CODE = "Code1";
                Severity SERVERITY = Severity.ERROR;
                String   MESSAGE = "Message1";
                String   KEY = "key";
                String   VALUE = "value";
                String   KEY2 = "key2";
                String   VALUE2 = "value2";


                Message newMessage = new Message(CODE, Severity.ERROR, MESSAGE);
                newMessage = newMessage.withParameter(KEY, VALUE);
                newMessage.addParameter(KEY2, VALUE2);

                assertEquals( CODE, newMessage.getCode());
                assertEquals( SERVERITY, newMessage.getSeverity());
                assertEquals( MESSAGE, newMessage.getDescription());
                assertEquals( VALUE, newMessage.getParameters().get(KEY));
                assertEquals( VALUE2, newMessage.getParameters().get(KEY2));
        }


}

