package ru.avito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для создания или обновления объявления")
public class CreateOrUpdateAdDto {
    @Schema(description = "Заголовок", example = "Продам слона", required = true)
    private String title;

    @Schema(description = "Цена", example = "100500", required = true)
    private Integer price;

    @Schema(description = "Описание", example = "Здоровый, серый")
    private String description;
}
