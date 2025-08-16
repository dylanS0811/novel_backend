package com.novelgrain.domain.booklist;

import java.util.List;

public interface BookListRepository {
    List<BookList> listByUser(Long userId);

    BookList create(Long userId, String name, String intro);

    BookList update(Long id, String name, String intro);

    void delete(Long id);

    List<BookListBook> listBooks(Long listId);

    BookListBook addBook(Long listId, BookListBook book);

    BookListBook updateBook(Long listId, Long bookId, BookListBook patch);

    void deleteBook(Long listId, Long bookId);

    void moveBook(Long fromListId, Long bookId, Long toListId);
}
