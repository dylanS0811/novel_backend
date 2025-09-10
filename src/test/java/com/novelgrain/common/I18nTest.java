package com.novelgrain.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class I18nTest {

    @Autowired
    MockMvc mvc;

    @Test
    void returnsEnglishTranslationWhenAcceptLanguageProvided() throws Exception {
        mvc.perform(get("/api/meta/categories").header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Romance")));
    }

    @Test
    void returnsEnglishTranslationWhenQueryParamProvided() throws Exception {
        mvc.perform(get("/api/meta/categories?lang=en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Romance")));
    }
}
