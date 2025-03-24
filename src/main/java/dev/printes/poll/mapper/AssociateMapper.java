package dev.printes.poll.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import dev.printes.poll.model.dto.AssociateDTO;
import dev.printes.poll.model.entity.Associate;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AssociateMapper {

    @Mapping(source = "key", target = "apiKey", qualifiedByName = "toUUID")
    Associate toEntity(AssociateDTO dto);

    @Mapping(target = "key", expression = "java(entity.getApiKey().toString())")
    AssociateDTO toDTO(Associate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "key", target = "apiKey", qualifiedByName = "toUUID")
    void merge(AssociateDTO associateDTO, @MappingTarget Associate associate);

    @Named("toUUID")
    static UUID toUUID(String key) {
        return UUID.fromString(key);
    }

}
