package com.senacor.tecco.ilms.common.context;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ServicesRequestContextHolder.
 */
public final class ServicesRequestContextHolder {
    /**
     * The Constant CONTEXT_HOLDER.
     */
    private static final ThreadLocal<ServicesRequestContextHolder> CONTEXT_HOLDER = new ThreadLocal<ServicesRequestContextHolder>();

    /**
     * The request id.
     */
    private final String requestID;

    /**
     * The client id.
     */
    private final String clientID;

    /**
     * The user name.
     */
    private final String userName;

    /**
     * The authorities.
     */
    private final List<String> authorities = new ArrayList<>(0);

    /**
     * The session id.
     */
    private final String sessionID;

    private ServicesRequestContextHolder(Builder builder) {
        requestID = builder.requestID;
        clientID = builder.clientID;
        userName = builder.userName;
        sessionID = builder.sessionID;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(ServicesRequestContextHolder copy) {
        Builder builder = new Builder();
        builder.requestID = copy.requestID;
        builder.clientID = copy.clientID;
        builder.userName = copy.userName;
        builder.sessionID = copy.sessionID;
        return builder;
    }

    // Getter/Setter

    /**
     * Gets the request id.
     *
     * @return the request id
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Gets the client id.
     *
     * @return the client id
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the authorities.
     *
     * @return the authorities
     */
    public List<String> getAuthorities() {
        return authorities;
    }

    /**
     * Gets the session id.
     *
     * @return the session id
     */
    public String getSessionID() {
        return sessionID;
    }

    // Context Handling

    /**
     * Gets the.
     *
     * @return the services request context holder
     */
    public static ServicesRequestContextHolder get() {
        ServicesRequestContextHolder ctx = CONTEXT_HOLDER.get();
        if (null == ctx) {
            ctx = empty();
            CONTEXT_HOLDER.set(ctx);
        }
        return ctx;
    }

    /**
     * Empty.
     *
     * @return the services request context holder
     */
    private static ServicesRequestContextHolder empty() {
        return ServicesRequestContextHolder.newBuilder().build();
    }

    /**
     * Sets the.
     *
     * @param ctx the ctx
     */
    public static void set(ServicesRequestContextHolder ctx) {
        CONTEXT_HOLDER.set(ctx);
    }

    /**
     * Clear.
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    public static final class Builder {
        private String requestID;
        private String clientID;
        private String userName;
        private List<String> authorities;
        private String sessionID;

        private Builder() {
        }

        public Builder withRequestID(String val) {
            requestID = val;
            return this;
        }

        public Builder withClientID(String val) {
            clientID = val;
            return this;
        }

        public Builder withUserName(String val) {
            userName = val;
            return this;
        }

        public Builder withAuthorities(List<String> val) {
            authorities = val;
            return this;
        }

        public Builder withSessionID(String val) {
            sessionID = val;
            return this;
        }

        public ServicesRequestContextHolder build() {
            return new ServicesRequestContextHolder(this);
        }
    }
}
