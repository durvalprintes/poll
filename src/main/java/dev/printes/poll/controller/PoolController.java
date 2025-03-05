package dev.printes.poll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.printes.poll.model.dto.PollRequestDTO;
import dev.printes.poll.model.entity.Associate;
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
        var associate = (Associate) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var id = pollService.createPoll(dto, associate.getEmail());
        var location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }


}
