package com.senacor.tecco.ilms.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dr. Michael Menzel, Senacor Technologies AG
 * <p>
 * Messages are returned with each REST response to provide detailed information concerning the response
 * In case of HTTP errors, error messages provide detailed information. Warnings are used to indicate a
 * reduced QoS or impediments in case of successful responses. E.g. limited set of results returned.
 */
public class Message {

    /**
     * Message code that can be used by the frontend to print a specific description
     */
    private String code;

    /**
     * Message classification concerning severity of the description (Info, Warning, Error)
     */
    private Severity severity;

    /**
     * Message description that can be used for logging
     */
    private String description;

    /**
     * Message details e.g. user id that can be used for logging
     */
    private Map<String, String> parameters = new HashMap<>();


    /**
     * Message Constructors
     */
    public Message() {
    }

    public Message(String code, Severity severity) {
        this.code = code;
        this.severity = severity;
    }

    public Message(String code, Severity severity, String description) {
        this.code = code;
        this.severity = severity;
        this.description = description;
    }

    /*
         * Getter and Setter
         */
    public String getCode() {
        return code;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map getParameters() {
        return parameters;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    @XmlTransient
    @JsonIgnore
    public String getCodePlusSeverity() {
        return code + "." + ((severity != null) ? severity.name() : "null");
    }

    public Message withParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code='" + code + '\'' +
                ", severity=" + severity +
                ", description='" + description + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    /*
         * Builder to construct a description
         */
    public static class Builder {
        private String code = "";
        private Severity severity = Severity.INFO;
        private String description = "";

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder() {
        }

        public Message build() {
            return new Message(code, severity, description);
        }
    }

}
