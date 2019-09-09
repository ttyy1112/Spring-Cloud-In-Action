package com.tm.scia.gateway.route;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;


public class StreamTest {

    @Test
    public void testSkip() {
        String path = "/chainet/poa/v1";
        String newPath = Arrays.stream(StringUtils.tokenizeToStringArray(path, "/"))
                .skip(1).collect(Collectors.joining("/"));
        Assert.assertEquals("poa/v1", newPath);
    }
}