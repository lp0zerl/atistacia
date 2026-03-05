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
import ru.avito.dto.CommentDto;
import ru.avito.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL)
    @Operation(summary = "Получить комментарии к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long adId) {
        return ResponseEntity.ok(commentService.getCommentsByAdId(adId));
    }

    @PostMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL)
    @Operation(summary = "Добавить комментарий к объявлению")
    @ApiResponse(responseCode = "201", description = "Комментарий добавлен")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long adId,
                                                 @RequestBody CommentDto comment,
                                                 Authentication authentication) {
        CommentDto created = commentService.addComment(adId, comment, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/ads/{adId}" + ApiConstants.COMMENTS_URL + "/{commentId}")
    @Operation(summary = "Удалить комментарий")
    @ApiResponse(responseCode = "204", description = "Комментарий удалён")
    @ApiResponse(responseCode = "403", description = "Нет прав")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    public ResponseEntity<Void> deleteComment(@PathVariable Long adId,
                                              @PathVariable Long commentId,
                                              Authentication authentication) {
        commentService.deleteComment(adId, commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}