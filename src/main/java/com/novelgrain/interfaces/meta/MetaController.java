package com.novelgrain.interfaces.meta;

import com.novelgrain.common.ApiResponse;
import com.novelgrain.common.Messages;
import com.novelgrain.domain.book.BookCategories;
import com.novelgrain.domain.book.BookOrientations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meta")
public class MetaController {

    record MetaItem(String key, String i18nKey, String name) { }

    @GetMapping("/categories")
    public ApiResponse<List<MetaItem>> categories() {
        var list = BookCategories.ALL.stream()
                .map(c -> new MetaItem(c, "category." + c, Messages.tr("category." + c)))
                .toList();
        return ApiResponse.ok(list);
    }

    @GetMapping("/orientations")
    public ApiResponse<List<MetaItem>> orientations() {
        var list = BookOrientations.ALL.stream()
                .map(o -> new MetaItem(o, "orientation." + o, Messages.tr("orientation." + o)))
                .toList();
        return ApiResponse.ok(list);
    }
}
