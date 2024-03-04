package com.generic.dummy;

import com.generic.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    final AuthClient authClient;
    @Autowired
    public TestController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/test")
    public String test() {
        return authClient.returnString();
    }
}
