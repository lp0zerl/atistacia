package ru.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.AdDto;
import ru.avito.dto.CreateOrUpdateAdDto;
import ru.avito.service.AdService;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADS_URL)
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {

    private final AdService adService;

    @GetMapping
    @Operation(summary = "Получить все объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<AdDto>> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping
    @Operation(summary = "Создать объявление")
    @ApiResponse(responseCode = "201", description = "Объявление создано")
    public ResponseEntity<AdDto> createAd(@RequestBody CreateOrUpdateAdDto ad, Authentication authentication) {
        AdDto createdAd = adService.createAd(ad, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить объявление по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<AdDto> getAd(@PathVariable Long id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить объявление")
    @ApiResponse(responseCode = "200", description = "Объявление обновлено")
    @ApiResponse(responseCode = "403", description = "Нет прав")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long id,
                                          @RequestBody CreateOrUpdateAdDto ad,
                                          Authentication authentication) {
        AdDto updatedAd = adService.updateAd(id, ad, authentication.getName());
        return ResponseEntity.ok(updatedAd);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @ApiResponse(responseCode = "204", description = "Объявление удалено")
    @ApiResponse(responseCode = "403", description = "Нет прав")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id, Authentication authentication) {
        adService.deleteAd(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}