package com.novelgrain.interfaces.book;

import com.novelgrain.application.book.BookUseCases;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.common.PageResponse;
import com.novelgrain.domain.book.Book;
import com.novelgrain.domain.book.Comment;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookUseCases use;

    @GetMapping
    public ApiResponse<PageResponse<Book>> list(
            @RequestParam(name = "tab", defaultValue = "new") String tab,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "orientation", required = false) String orientation,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ApiResponse.ok(use.list(tab, category, orientation, search, tag, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<Book> detail(@PathVariable("id") Long id) {
        return ApiResponse.ok(use.detail(id));
    }

    @PostMapping
    public ApiResponse<Book> create(@RequestBody CreateReq req) {
        var b = use.create(
                req.getTitle(), req.getAuthor(), req.getOrientation(),
                req.getCategory(), req.getBlurb(), req.getSummary(),
                req.getTags(), req.getCoverUrl(), req.getRecommenderId()
        );
        return ApiResponse.ok(b);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Book> update(@PathVariable("id") Long id, @RequestBody UpdateReq req) {
        var patch = Book.builder()
                .title(req.getTitle()).author(req.getAuthor()).orientation(req.getOrientation()).category(req.getCategory())
                .blurb(req.getBlurb()).summary(req.getSummary()).coverUrl(req.getCoverUrl()).tags(req.getTags()).build();
        return ApiResponse.ok(use.update(id, patch));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        use.delete(id, userId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/likes")
    public ApiResponse<Object> like(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        use.like(id, userId);
        var book = use.detail(id);
        return ApiResponse.ok(java.util.Map.of("liked", true, "likes", book.getLikes()));
    }

    @DeleteMapping("/{id}/likes")
    public ApiResponse<Object> unlike(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        use.unlike(id, userId);
        var book = use.detail(id);
        return ApiResponse.ok(java.util.Map.of("liked", false, "likes", book.getLikes()));
    }

    @PostMapping("/{id}/bookmarks")
    public ApiResponse<Object> bookmark(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        use.bookmark(id, userId);
        var book = use.detail(id);
        return ApiResponse.ok(java.util.Map.of("bookmarked", true, "bookmarks", book.getBookmarks()));
    }

    @DeleteMapping("/{id}/bookmarks")
    public ApiResponse<Object> unbookmark(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        use.unbookmark(id, userId);
        var book = use.detail(id);
        return ApiResponse.ok(java.util.Map.of("bookmarked", false, "bookmarks", book.getBookmarks()));
    }

    @GetMapping("/{id}/comments")
    public ApiResponse<PageResponse<Comment>> comments(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ApiResponse.ok(use.comments(id, userId, page, size));
    }

    @PostMapping("/{id}/comments")
    public ApiResponse<Comment> addComment(@PathVariable("id") Long id, @RequestBody CommentReq req) {
        return ApiResponse.ok(use.addComment(id, req.getUserId(), req.getText(), req.getParentId()));
    }

    /** === 按用户取“我点过赞”的书 ID 列表 ===
     * GET /api/likes?userId=123 -> { "ids": [1,2,3] }
     */
    @GetMapping("/likes")
    public ApiResponse<Object> userLikes(@RequestParam("userId") Long userId) {
        List<Long> ids = use.listLikedBookIds(userId);
        return ApiResponse.ok(java.util.Map.of("ids", ids));
    }

    /** === 按用户取“我收藏过”的书 ID 列表 ===
     * GET /api/bookmarks?userId=123 -> { "ids": [5,8,13] }
     */
    @GetMapping("/bookmarks")
    public ApiResponse<Object> userBookmarks(@RequestParam("userId") Long userId) {
        List<Long> ids = use.listBookmarkedBookIds(userId);
        return ApiResponse.ok(java.util.Map.of("ids", ids));
    }

    @Data
    public static class CreateReq {
        @NotBlank
        private String title;

        private String author;

        @NotBlank
        private String orientation;

        @NotBlank
        private String category;

        private String blurb;

        private String summary;

        private java.util.List<String> tags;

        private String coverUrl;

        private Long recommenderId;
    }

    @Data
    public static class UpdateReq {
        private String title;

        private String author;

        private String orientation;

        private String category;

        private String blurb;

        private String summary;

        private java.util.List<String> tags;

        private String coverUrl;
    }

    @Data
    public static class CommentReq {
        @NotBlank
        private String text;

        private Long parentId;

        private Long userId;
    }
}
