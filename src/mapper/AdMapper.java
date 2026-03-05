package ru.avito.mapper;

import org.mapstruct.*;
import ru.avito.dto.AdDto;
import ru.avito.dto.CreateOrUpdateAdDto;
import ru.avito.entity.Ad;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AdMapper {

    @Mapping(source = "author.email", target = "author")
    @Mapping(source = "id", target = "pk")
    AdDto toDto(Ad ad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Ad toEntity(CreateOrUpdateAdDto createOrUpdateAdDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAdFromDto(CreateOrUpdateAdDto dto, @MappingTarget Ad ad);
}