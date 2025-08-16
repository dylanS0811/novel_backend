package com.novelgrain.interfaces.booklist;

import com.novelgrain.application.booklist.BookListUseCases;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.domain.booklist.BookList;
import com.novelgrain.domain.booklist.BookListBook;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookListController {
    private final BookListUseCases use;

    @GetMapping("/api/users/{userId}/sheets")
    public ApiResponse<List<BookList>> list(@PathVariable Long userId) {
        return ApiResponse.ok(use.list(userId));
    }

    @PostMapping("/api/users/{userId}/sheets")
    public ApiResponse<BookList> create(@PathVariable Long userId, @RequestBody SheetCreateReq req) {
        return ApiResponse.ok(use.create(userId, req.getName()));
    }

    @PatchMapping("/api/sheets/{id}")
    public ApiResponse<BookList> rename(@PathVariable Long id, @RequestBody SheetCreateReq req) {
        return ApiResponse.ok(use.rename(id, req.getName()));
    }

    @DeleteMapping("/api/sheets/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        use.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/api/sheets/{id}/books")
    public ApiResponse<List<BookListBook>> listBooks(@PathVariable Long id) {
        return ApiResponse.ok(use.listBooks(id));
    }

    @PostMapping("/api/sheets/{id}/books")
    public ApiResponse<BookListBook> addBook(@PathVariable Long id, @RequestBody SheetBookCreateReq req) {
        var book = BookListBook.builder()
                .bookId(req.getBookId())
                .title(req.getTitle())
                .author(req.getAuthor())
                .orientation(req.getOrientation())
                .category(req.getCategory())
                .rating(req.getRating())
                .review(req.getReview())
                .build();
        return ApiResponse.ok(use.addBook(id, book));
    }

    @PatchMapping("/api/sheets/{id}/books/{bookId}")
    public ApiResponse<BookListBook> updateBook(@PathVariable Long id, @PathVariable("bookId") Long bookId, @RequestBody SheetBookUpdateReq req) {
        var patch = BookListBook.builder()
                .bookId(req.getBookId())
                .title(req.getTitle())
                .author(req.getAuthor())
                .orientation(req.getOrientation())
                .category(req.getCategory())
                .rating(req.getRating())
                .review(req.getReview())
                .build();
        return ApiResponse.ok(use.updateBook(id, bookId, patch));
    }

    @DeleteMapping("/api/sheets/{id}/books/{bookId}")
    public ApiResponse<Object> deleteBook(@PathVariable Long id, @PathVariable("bookId") Long bookId) {
        use.deleteBook(id, bookId);
        return ApiResponse.ok(null);
    }

    @Data
    public static class SheetCreateReq {
        private String name;
    }

    @Data
    public static class SheetBookCreateReq {
        private Long bookId;
        private String title;
        private String author;
        private String orientation;
        private String category;
        private Integer rating;
        private String review;
    }

    @Data
    public static class SheetBookUpdateReq {
        private Long bookId;
        private String title;
        private String author;
        private String orientation;
        private String category;
        private Integer rating;
        private String review;
    }
}
