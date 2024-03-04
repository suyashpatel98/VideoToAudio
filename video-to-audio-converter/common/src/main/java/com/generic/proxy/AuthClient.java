package com.generic.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "auth-client", url = "${auth.service.url}")
public interface AuthClient {

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestParam(name = "emailId") String email,
                                    @RequestParam(name = "password") String password);

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestParam(name = "emailId") String email,
                                 @RequestParam(name = "password") String password);

    @GetMapping("/validate")
    ResponseEntity<String> validateToken(@RequestHeader(name = "Authorization") String tokenHeader);
}
