package com.novelgrain.interfaces.comment;

import com.novelgrain.application.book.BookUseCases;
import com.novelgrain.domain.book.Comment;
import com.novelgrain.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final BookUseCases use;

    @PostMapping("/{id}/likes")
    public ApiResponse<Comment> like(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        return ApiResponse.ok(use.likeComment(id, userId));
    }

    @DeleteMapping("/{id}/likes")
    public ApiResponse<Comment> unlike(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        return ApiResponse.ok(use.unlikeComment(id, userId));
    }
}
