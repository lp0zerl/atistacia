package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.avito.dto.CommentDto;
import ru.avito.entity.Comment;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {

    @Mapping(source = "author.email", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentDto commentDto);
}