package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Объявление")
public class AdDto {
    @Schema(description = "ID объявления", example = "1")
    private Integer pk;

    @Schema(description = "Заголовок", example = "Продам слона")
    private String title;

    @Schema(description = "Цена", example = "100500")
    private Integer price;

    @Schema(description = "Автор объявления", example = "user@mail.ru")
    private String author;
}