package dev.printes.poll.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.printes.poll.exception.ConflictException;
import dev.printes.poll.mapper.AssociateMapper;
import dev.printes.poll.model.dto.AssociateDTO;
import dev.printes.poll.model.entity.Associate;
import dev.printes.poll.repository.AssociateRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AssociateService implements UserDetailsService {

    private final AssociateRepository repository;
    private final PollValidator validator;
    private final AssociateMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
        return repository.findByApiKey(UUID.fromString(apiKey))
            .orElseThrow(() -> new UsernameNotFoundException("Associate not found"));
    }

    public List<AssociateDTO> getAssociates() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public Long createAssociate(AssociateDTO dto) {
        return repository.save(mapper.toEntity(dto)).getId();
    }

    public AssociateDTO getAssociate(Long id) {
        return mapper.toDTO(this.findById(id));
    }

    public void updateAssociate(Long id, AssociateDTO dto) {
        validator.checkAssociteFields(dto);
        var associate = this.findById(id);
        mapper.merge(dto, associate);
        repository.save(associate);
    }

    public void removeAssociate(Long id) {
        var associate = this.findById(id);
        repository.delete(associate);
    }

    private Associate findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ConflictException("Associado não encontrado"));
    }

}
