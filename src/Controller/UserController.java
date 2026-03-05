package ru.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.NewPassword;
import ru.avito.dto.UserDto;
import ru.avito.service.UserService;

@RestController
@RequestMapping(ApiConstants.USERS_URL)
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Получить информацию о текущем пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Не авторизован")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        UserDto user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновить информацию о текущем пользователе")
    @ApiResponse(responseCode = "200", description = "Информация обновлена")
    @ApiResponse(responseCode = "401", description = "Не авторизован")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto userDto,
                                                     Authentication authentication) {
        UserDto updated = userService.updateUser(authentication.getName(), userDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/set_password")
    @Operation(summary = "Сменить пароль")
    @ApiResponse(responseCode = "200", description = "Пароль изменён")
    @ApiResponse(responseCode = "400", description = "Неверный старый пароль или некорректные данные")
    @ApiResponse(responseCode = "401", description = "Не авторизован")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword,
                                         Authentication authentication) {
        userService.changePassword(authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword());
        return ResponseEntity.ok().build();
    }
}