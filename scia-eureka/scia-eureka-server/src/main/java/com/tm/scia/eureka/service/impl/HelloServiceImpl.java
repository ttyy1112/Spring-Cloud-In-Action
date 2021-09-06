package com.tm.scia.eureka.service.impl;

import com.tm.scia.eureka.service.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/helloService")
public class HelloServiceImpl implements HelloService {
    @Override
    @ResponseBody
    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public String sayHello(@RequestParam("userName") String userName) {
        return "Hello " + userName;
    }
}
