package dev.printes.poll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.printes.poll.service.PollFacade;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final PollFacade pollService;

    @GetMapping("/poll/result")
    public ResponseEntity<Object> getPollResult(
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "size", required = false, defaultValue = "10") int size,
        @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(name = "sortDir", required = false, defaultValue = "ASC") String sortDir) {
        var result = pollService.getPollResult(page, size, sortBy, sortDir);
        return ResponseEntity.ok(result);
    }

}
