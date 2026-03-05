package ru.avito.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.avito.dto.RegisterReqDto;
import ru.avito.dto.UserDto;
import ru.avito.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "username", target = "email")
    User registerToEntity(RegisterReqDto registerReqDto);
}