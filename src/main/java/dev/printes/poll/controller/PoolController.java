package dev.printes.poll.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/poll")
public class PoolController {

    @GetMapping("/auth")
    public String authentication() {
        return "API Key authentication successful!";
    }

}
