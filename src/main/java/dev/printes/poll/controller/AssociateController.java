package dev.printes.poll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.printes.poll.model.dto.AssociateDTO;
import dev.printes.poll.model.validation.CreateValidation;
import dev.printes.poll.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/associate")
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService associateService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(associateService.getAssociates());
    }

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestBody @Validated(CreateValidation.class) AssociateDTO dto) {
        var id = associateService.createAssociate(dto);
        var location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .replacePath("/api/v1/associate/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(associateService.getAssociate(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id,
        @RequestBody @Valid AssociateDTO dto) {
        associateService.updateAssociate(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@PathVariable("id") Long id) {
        associateService.removeAssociate(id);
        return ResponseEntity.noContent().build();
    }

}
