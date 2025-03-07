package dev.printes.poll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.service.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/poll")
@RequiredArgsConstructor
public class PoolController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<Object> createPoll(@RequestBody @Valid PollRequestDTO dto) {
        var id = pollService.createPoll(dto);
        var location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .replacePath("/api/v1/poll/{id}/session")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/session")
    public ResponseEntity<Object> createPollSession(
        @PathVariable("id") Long pollId,
        @RequestParam(name = "closedDate", required = false) String closedDate) {
        pollService.createPollSession(pollId, closedDate);
        var location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .replacePath("/api/v1/poll/{id}/session/voting")
            .buildAndExpand(pollId)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/session/voting")
    public ResponseEntity<Object> registerVoting(
        @PathVariable("id") Long pollId,
        @RequestParam(name = "vote", required = true) String vote) {
        pollService.registerVoting(pollId, vote);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/session/close")
    public ResponseEntity<Object> closePollSession(
        @PathVariable("id") Long pollId) {
        pollService.closePollSession(pollId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/session/result")
    public ResponseEntity<Object> getPollResult(
        @PathVariable("id") Long pollId) {
        var result = pollService.getPollResult(pollId);
        return ResponseEntity.ok(result);
    }


}
