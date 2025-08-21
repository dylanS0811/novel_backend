package com.novelgrain.interfaces.tag;

import com.novelgrain.application.tag.TagUseCases;
import com.novelgrain.common.ApiResponse;
import com.novelgrain.common.PageResponse;
import com.novelgrain.domain.tag.Tag;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagUseCases use;

    @GetMapping
    public ApiResponse<PageResponse<Tag>> list(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", defaultValue = "hot") String sort
    ) {
        return ApiResponse.ok(use.list(search, page, size, sort));
    }

    @GetMapping("/suggest")
    public ApiResponse<Object> suggest(@RequestParam String q) {
        return ApiResponse.ok(use.suggest(q));
    }

    @PostMapping
    public ApiResponse<Object> create(@RequestBody CreateReq req) {
        return ApiResponse.ok(java.util.Map.of("name", use.create(req.getName())));
    }

    @Data
    public static class CreateReq {
        private String name;
    }
}
