package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Смена пароля")
public class NewPassword {
    @Schema(description = "Текущий пароль", required = true)
    private String currentPassword;

    @Schema(description = "Новый пароль", required = true)
    private String newPassword;
}