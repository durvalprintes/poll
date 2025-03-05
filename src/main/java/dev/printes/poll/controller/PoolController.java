package dev.printes.poll.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.printes.poll.model.entity.Associate;

@RestController
@RequestMapping("/api/v1/poll")
public class PoolController {

    @GetMapping("/auth")
    public String authentication() {
        var associate = (Associate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return String.format("Associate %s successfully authenticated!", associate.getName());
    }

}
