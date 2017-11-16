package com.senacor.tecco.ilms.wikiloader.common;

public enum MessageCode {

    USER_CREATED("UserCreated"),
    USER_DELETED("UserRetrieved"),
    USER_UPDATED("UserUupdated"),
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    INVALID_USER_ID("Invalid User Id");

    private final String text;

    private MessageCode(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}


