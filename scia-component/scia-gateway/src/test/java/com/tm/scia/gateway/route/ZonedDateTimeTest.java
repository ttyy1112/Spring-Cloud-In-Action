package com.tm.scia.gateway.route;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;


public class ZonedDateTimeTest {

    @Test
    public void testZonedDateTime() {
        System.out.println(ZonedDateTime.now().toString());
    }
}