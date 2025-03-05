package dev.printes.poll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            .fromCurrentRequest()
            .path("/{id}/session")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/session")
    public ResponseEntity<Object> createPollSession(
        @PathVariable("id") Long pollId,
        @RequestParam(name = "closedDate", required = false) String closedDate) {
        var id = pollService.createPollSession(pollId, closedDate);
        var location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}/voting")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }


}
