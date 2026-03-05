package ru.avito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пользователь")
public class UserDto {
    @Schema(description = "ID пользователя", example = "1")
    private Integer id;

    @Schema(description = "Email", example = "user@mail.ru")
    private String email;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Schema(description = "Телефон", example = "+7-123-456-78-90")
    private String phone;
}