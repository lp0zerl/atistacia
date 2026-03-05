package ru.avito.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на логин")
public class LoginReqDto {
    @Schema(description = "Email", example = "user@mail.ru", required = true)
    private String username;

    @Schema(description = "Пароль", example = "password", required = true)
    private String password;
}
