package dev.elshan.cvBuilder.mapper;

import dev.elshan.cvBuilder.model.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "id", ignore = true) // Ignore ID to avoid overwriting
    void updateEntity(@MappingTarget Resume target, Resume source);
}
