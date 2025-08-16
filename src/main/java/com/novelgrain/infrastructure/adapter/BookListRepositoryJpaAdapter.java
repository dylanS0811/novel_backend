package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.booklist.BookList;
import com.novelgrain.domain.booklist.BookListBook;
import com.novelgrain.domain.booklist.BookListRepository;
import com.novelgrain.infrastructure.jpa.entity.BookListBookPO;
import com.novelgrain.infrastructure.jpa.entity.BookListPO;
import com.novelgrain.infrastructure.jpa.repo.BookListBookJpa;
import com.novelgrain.infrastructure.jpa.repo.BookListJpa;
import com.novelgrain.infrastructure.jpa.repo.UserJpa;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BookListRepositoryJpaAdapter implements BookListRepository {
    private final BookListJpa listJpa;
    private final BookListBookJpa bookJpa;
    private final UserJpa userJpa;

    @Transactional(readOnly = true)
    @Override
    public List<BookList> listByUser(Long userId) {
        return listJpa.findByUser_Id(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookList create(Long userId, String name, String intro) {
        var user = userJpa.findById(userId).orElseThrow();
        var po = BookListPO.builder().user(user).name(name).intro(intro).build();
        po = listJpa.save(po);
        return toDomain(po);
    }

    @Transactional
    @Override
    public BookList update(Long id, String name, String intro) {
        var po = listJpa.findById(id).orElseThrow();
        if (name != null) po.setName(name);
        if (intro != null) po.setIntro(intro);
        po = listJpa.save(po);
        return toDomain(po);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        listJpa.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookListBook> listBooks(Long listId) {
        return bookJpa.findByBookList_Id(listId).stream().map(this::toDomainBook).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookListBook addBook(Long listId, BookListBook book) {
        var list = listJpa.findById(listId).orElseThrow();
        var po = BookListBookPO.builder()
                .bookList(list)
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .orientation(book.getOrientation())
                .category(book.getCategory())
                .rating(book.getRating())
                .review(book.getReview())
                .build();
        po = bookJpa.save(po);
        return toDomainBook(po);
    }

    @Transactional
    @Override
    public BookListBook updateBook(Long listId, Long bookId, BookListBook patch) {
        var po = bookJpa.findById(bookId).orElseThrow();
        if (patch.getBookId() != null) po.setBookId(patch.getBookId());
        if (patch.getTitle() != null) po.setTitle(patch.getTitle());
        if (patch.getAuthor() != null) po.setAuthor(patch.getAuthor());
        if (patch.getOrientation() != null) po.setOrientation(patch.getOrientation());
        if (patch.getCategory() != null) po.setCategory(patch.getCategory());
        if (patch.getRating() != null) po.setRating(patch.getRating());
        if (patch.getReview() != null) po.setReview(patch.getReview());
        po = bookJpa.save(po);
        return toDomainBook(po);
    }

    @Transactional
    @Override
    public void deleteBook(Long listId, Long bookId) {
        bookJpa.deleteById(bookId);
    }

    @Transactional
    @Override
    public void moveBook(Long fromListId, Long bookId, Long toListId) {
        if (fromListId.equals(toListId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "same list");
        }
        var entry = bookJpa.findById(bookId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        if (!entry.getBookList().getId().equals(fromListId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        var target = listJpa.findById(toListId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        if (!entry.getBookList().getUser().getId().equals(target.getUser().getId())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN);
        }
        if (bookJpa.existsByBookList_IdAndBookId(toListId, entry.getBookId())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT);
        }
        entry.setBookList(target);
        bookJpa.save(entry);
    }

    private BookList toDomain(BookListPO po) {
        return BookList.builder()
                .id(po.getId())
                .userId(po.getUser().getId())
                .name(po.getName())
                .intro(po.getIntro())
                .createdAt(po.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .updatedAt(po.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }

    private BookListBook toDomainBook(BookListBookPO po) {
        return BookListBook.builder()
                .id(po.getId())
                .bookListId(po.getBookList().getId())
                .bookId(po.getBookId())
                .title(po.getTitle())
                .author(po.getAuthor())
                .orientation(po.getOrientation())
                .category(po.getCategory())
                .rating(po.getRating())
                .review(po.getReview())
                .createdAt(po.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .updatedAt(po.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }
}
