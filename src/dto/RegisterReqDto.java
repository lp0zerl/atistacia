package ru.avito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterReqDto {
    @Schema(description = "Email", example = "user@mail.ru", required = true)
    private String username;

    @Schema(description = "Пароль", example = "password", required = true)
    private String password;

    @Schema(description = "Имя", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Телефон", example = "+7-123-456-78-90", required = true)
    private String phone;
}