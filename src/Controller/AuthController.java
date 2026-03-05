package ru.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.LoginReqDto;
import ru.avito.dto.RegisterReqDto;
import ru.avito.service.AuthService;

@RestController
@RequestMapping(ApiConstants.AUTH_URL)
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Вход в систему")
    @ApiResponse(responseCode = "200", description = "Успешный вход")
    @ApiResponse(responseCode = "401", description = "Неверные учётные данные")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReq) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto registerReq) {
        authService.register(registerReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}