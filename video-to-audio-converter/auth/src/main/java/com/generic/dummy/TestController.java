package com.generic.dummy;

import com.generic.proxy.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    final AuthClient authClient;
    @Autowired
    public TestController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return authClient.register("suyash", "sljfkg");
    }
}
