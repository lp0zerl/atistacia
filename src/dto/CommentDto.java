package ru.avito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Комментарий")
public class CommentDto {
    @Schema(description = "ID комментария", example = "10")
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Отличный товар!")
    private String text;

    @Schema(description = "Автор комментария", example = "user@mail.ru")
    private String author;

    @Schema(description = "Дата создания", example = "2023-12-01T12:00:00")
    private String createdAt;
}