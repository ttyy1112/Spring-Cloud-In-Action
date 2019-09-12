package com.tm.scia.webflux.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SNController {
    @RequestMapping("/sn")
    public Mono<String> processPartnerSn(@RequestParam("partnerCode") String partnerCode) {
        return Mono.just("Receive sn bill of partner with code " + partnerCode);
    }

}
