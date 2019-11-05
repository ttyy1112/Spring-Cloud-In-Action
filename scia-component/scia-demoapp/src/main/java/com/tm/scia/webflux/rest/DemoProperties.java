package com.tm.scia.webflux.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private Map<String, String> args;

    private String[] array;


    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }


    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }
}
