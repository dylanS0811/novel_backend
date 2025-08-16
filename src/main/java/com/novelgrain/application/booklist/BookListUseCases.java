package com.novelgrain.application.booklist;

import com.novelgrain.domain.booklist.BookList;
import com.novelgrain.domain.booklist.BookListBook;
import com.novelgrain.domain.booklist.BookListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookListUseCases {
    private final BookListRepository repo;

    public List<BookList> list(Long userId) {
        return repo.listByUser(userId);
    }

    public BookList create(Long userId, String name) {
        return repo.create(userId, name);
    }

    public BookList rename(Long id, String name) {
        return repo.rename(id, name);
    }

    public void delete(Long id) {
        repo.delete(id);
    }

    public List<BookListBook> listBooks(Long listId) {
        return repo.listBooks(listId);
    }

    public BookListBook addBook(Long listId, BookListBook book) {
        return repo.addBook(listId, book);
    }

    public BookListBook updateBook(Long listId, Long bookId, BookListBook patch) {
        return repo.updateBook(listId, bookId, patch);
    }

    public void deleteBook(Long listId, Long bookId) {
        repo.deleteBook(listId, bookId);
    }
}
