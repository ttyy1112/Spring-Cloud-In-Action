package com.tm.scia.eureka.feign;

import com.tm.scia.eureka.service.HelloService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "Eureka-Server", contextId = "helloService")
public interface HelloFeignService extends HelloService {
    @Override
    @RequestMapping(value = "/service/helloService/sayHello", method = RequestMethod.GET)
    @ResponseBody
    String sayHello(@RequestParam("userName") String userName);
}
