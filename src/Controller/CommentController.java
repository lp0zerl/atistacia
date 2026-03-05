package Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avito.constants.ApiConstants;
import ru.avito.dto.CommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    @GetMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL)
    @Operation(summary = "Получить комментарии к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Integer adId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL)
    @Operation(summary = "Добавить комментарий к объявлению")
    @ApiResponse(responseCode = "201", description = "Комментарий добавлен")
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer adId,
                                                 @RequestBody CommentDto comment) {
        CommentDto stub = new CommentDto();
        stub.setPk(0);
        stub.setText(comment.getText());
        stub.setAuthor("user@example.com");
        stub.setCreatedAt(java.time.LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(stub);
    }

    @DeleteMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL + "/{commentId}")
    @Operation(summary = "Удалить комментарий")
    @ApiResponse(responseCode = "204", description = "Комментарий удалён")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        return ResponseEntity.noContent().build();
    }
}