package com.senacor.tecco.ilms.common.context.demo;

/**
 * @author Andreas Keefer
 */
public class Demo {
    private String msg;
    private String name;

    public Demo() {
    }

    //@JsonCreator
    public Demo(/*@JsonProperty("msg")*/ String msg, /*@JsonProperty("name")*/ String name) {
        this.msg = msg;
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }
}
