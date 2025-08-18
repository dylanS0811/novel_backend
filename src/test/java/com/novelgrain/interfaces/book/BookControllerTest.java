package com.novelgrain.interfaces.book;

import com.novelgrain.domain.book.Book;
import com.novelgrain.domain.book.BookRepository;
import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.infrastructure.jpa.repo.UserJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserJpa userJpa;

    @Test
    void listByRecommenderIdReturnsOnlyTheirBooks() throws Exception {
        var u1 = userJpa.save(UserPO.builder().username("u1").nick("n1").passwordHash("p").build());
        var u2 = userJpa.save(UserPO.builder().username("u2").nick("n2").passwordHash("p").build());
        bookRepo.save(Book.builder().title("t1").orientation("o").category("爱情").build(), u1.getId());
        bookRepo.save(Book.builder().title("t2").orientation("o").category("爱情").build(), u2.getId());

        mvc.perform(get("/api/books").param("recommenderId", u1.getId().toString())
                .param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list.length()").value(1))
                .andExpect(jsonPath("$.data.list[0].recommender.id").value(u1.getId()))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.total").value(1));
    }

    @Test
    void listByNonExistingRecommenderIdReturnsEmptyList() throws Exception {
        mvc.perform(get("/api/books").param("recommenderId", "9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list.length()").value(0))
                .andExpect(jsonPath("$.data.total").value(0));
    }

    @Test
    void fuzzySearchMatchesPartialKeywords() throws Exception {
        var u = userJpa.save(UserPO.builder().username("u3").nick("n3").passwordHash("p").build());
        bookRepo.save(Book.builder().title("Alpha").orientation("o").category("爱情").blurb("desc").build(), u.getId());
        bookRepo.save(Book.builder().title("Bravo").orientation("o").category("爱情").blurb("desc").build(), u.getId());

        mvc.perform(get("/api/books").param("search", "alp").param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list.length()").value(1))
                .andExpect(jsonPath("$.data.list[0].title").value("Alpha"));

        mvc.perform(get("/api/books").param("search", "ALP").param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list.length()").value(1))
                .andExpect(jsonPath("$.data.list[0].title").value("Alpha"));
    }

    @Test
    void createWithInvalidCategoryReturnsBadRequest() throws Exception {
        var u = userJpa.save(UserPO.builder().username("u4").nick("n4").passwordHash("p").build());
        mvc.perform(post("/api/books")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"t","orientation":"o","category":"invalid","recommenderId":%d}
                                """.formatted(u.getId())))
                .andExpect(status().isBadRequest());
    }
}
