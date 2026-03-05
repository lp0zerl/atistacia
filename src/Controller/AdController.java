package Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.AdDto;
import ru.avito.dto.CreateOrUpdateAdDto;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADS_URL)
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdController {

    @GetMapping
    @Operation(summary = "Получить все объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<AdDto>> getAllAds() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    @Operation(summary = "Создать объявление")
    @ApiResponse(responseCode = "201", description = "Объявление создано")
    public ResponseEntity<AdDto> createAd(@RequestBody CreateOrUpdateAdDto ad) {
        AdDto stub = new AdDto();
        stub.setPk(0);
        stub.setTitle(ad.getTitle());
        stub.setPrice(ad.getPrice());
        stub.setAuthor("user@example.com"); // заглушка
        return ResponseEntity.status(HttpStatus.CREATED).body(stub);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить объявление по ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<AdDto> getAd(@PathVariable Integer id) {
        // Заглушка: всегда возвращаем пустой объект
        return ResponseEntity.ok(new AdDto());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить объявление")
    @ApiResponse(responseCode = "200", description = "Объявление обновлено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<AdDto> updateAd(@PathVariable Integer id,
                                          @RequestBody CreateOrUpdateAdDto ad) {
        AdDto stub = new AdDto();
        stub.setPk(id);
        stub.setTitle(ad.getTitle());
        stub.setPrice(ad.getPrice());
        stub.setAuthor("user@example.com"); // заглушка
        return ResponseEntity.ok(stub);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @ApiResponse(responseCode = "204", description = "Объявление удалено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}