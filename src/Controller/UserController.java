package ru.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.UserDto;

@RestController
@RequestMapping(ApiConstants.USERS_URL)
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Получить информацию о текущем пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Не авторизован")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto stub = new UserDto();
        stub.setId(1);
        stub.setEmail("user@mail.ru");
        stub.setFirstName("Иван");
        stub.setLastName("Иванов");
        stub.setPhone("+7-123-456-78-90");
        return ResponseEntity.ok(stub);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновить информацию о текущем пользователе")
    @ApiResponse(responseCode = "200", description = "Информация обновлена")
    @ApiResponse(responseCode = "401", description = "Не авторизован")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto user) {
        user.setId(1);
        return ResponseEntity.ok(user);
    }
}