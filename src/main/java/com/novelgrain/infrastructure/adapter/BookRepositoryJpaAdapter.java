package com.novelgrain.infrastructure.adapter;

import com.novelgrain.domain.book.Book;
import com.novelgrain.domain.book.BookRepository;
import com.novelgrain.domain.book.Comment;
import com.novelgrain.infrastructure.jpa.entity.BookBookmarkPO;
import com.novelgrain.infrastructure.jpa.entity.BookLikePO;
import com.novelgrain.infrastructure.jpa.entity.BookPO;
import com.novelgrain.infrastructure.jpa.entity.CommentPO;
import com.novelgrain.infrastructure.jpa.entity.CommentLikePO;
import com.novelgrain.infrastructure.jpa.entity.TagPO;
import com.novelgrain.infrastructure.jpa.repo.BookBookmarkJpa;
import com.novelgrain.infrastructure.jpa.repo.BookJpa;
import com.novelgrain.infrastructure.jpa.repo.BookLikeJpa;
import com.novelgrain.infrastructure.jpa.repo.CommentJpa;
import com.novelgrain.infrastructure.jpa.repo.CommentLikeJpa;
import com.novelgrain.infrastructure.jpa.repo.TagJpa;
import com.novelgrain.infrastructure.jpa.repo.UserJpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BookRepositoryJpaAdapter implements BookRepository {

    private final BookJpa bookJpa;

    private final TagJpa tagJpa;

    private final UserJpa userJpa;

    private final CommentJpa commentJpa;

    private final CommentLikeJpa commentLikeJpa;

    private final BookLikeJpa likeJpa;

    private final BookBookmarkJpa bookmarkJpa;

    @Transactional(readOnly = true)
    @Override
    public Page<Book> page(String tab, String category, String orientation, String search, String tag,
                           Long recommenderId, String recommender, int page, int size) {
        Specification<BookPO> spec = (Root<BookPO> root, CriteriaQuery<?> q, CriteriaBuilder cb) -> {
            var ps = new java.util.ArrayList<Predicate>();
            if (category != null && !category.isBlank() && !"全部".equals(category)) {
                ps.add(cb.equal(root.get("category"), category));
            }
            if (orientation != null && !orientation.isBlank() && !"全部".equals(orientation)) {
                ps.add(cb.equal(root.get("orientation"), orientation));
            }
            if (search != null && !search.isBlank()) {
                String like = "%" + search + "%";
                ps.add(cb.or(
                        cb.like(root.get("title"), like),
                        cb.like(root.get("author"), like),
                        cb.like(root.get("blurb"), like)
                ));
            }
            if (tag != null && !tag.isBlank()) {
                var join = root.join("tags");
                ps.add(cb.equal(join.get("name"), tag));
                q.distinct(true);
            }
            if (recommenderId != null) {
                ps.add(cb.equal(root.get("recommender").get("id"), recommenderId));
            } else if (recommender != null && !recommender.isBlank()) {
                ps.add(cb.equal(root.get("recommender").get("nick"), recommender));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };

        Sort sort = "hot".equalsIgnoreCase(tab)
                ? Sort.by(Sort.Order.desc("hot"), Sort.Order.desc("createdAt"))
                : Sort.by(Sort.Order.desc("createdAt"));

        var p = bookJpa.findAll(spec, PageRequest.of(page - 1, size, sort));
        return p.map(this::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Book findById(Long id) {
        return bookJpa.findByIdWithTags(id).map(this::toDomain).orElseThrow();
    }

    @Transactional
    @Override
    public Book save(Book b, Long recommenderId) {
        var user = userJpa.findById(recommenderId).orElseThrow();
        BookPO po = BookPO.builder()
                .title(b.getTitle()).author(b.getAuthor()).orientation(b.getOrientation()).category(b.getCategory())
                .blurb(b.getBlurb()).summary(b.getSummary())
                .recommender(user).likesCount(0).bookmarksCount(0).comments(0)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        if (b.getTags() != null) {
            var set = b.getTags().stream()
                    .map(t -> tagJpa.findByName(t)
                            .orElseGet(() -> tagJpa.save(TagPO.builder().name(t).createdAt(LocalDateTime.now()).build())))
                    .collect(Collectors.toSet());
            po.setTags(set);
        }
        po = bookJpa.save(po);
        return toDomain(po);
    }

    @Transactional
    @Override
    public Book update(Long id, Book patch) {
        var po = bookJpa.findById(id).orElseThrow();
        if (patch.getTitle() != null) po.setTitle(patch.getTitle());
        if (patch.getAuthor() != null) po.setAuthor(patch.getAuthor());
        if (patch.getOrientation() != null) po.setOrientation(patch.getOrientation());
        if (patch.getCategory() != null) po.setCategory(patch.getCategory());
        if (patch.getBlurb() != null) po.setBlurb(patch.getBlurb());
        if (patch.getSummary() != null) po.setSummary(patch.getSummary());
        if (patch.getTags() != null) {
            var set = patch.getTags().stream()
                    .map(t -> tagJpa.findByName(t)
                            .orElseGet(() -> tagJpa.save(TagPO.builder().name(t).createdAt(LocalDateTime.now()).build())))
                    .collect(Collectors.toSet());
            po.setTags(set);
        }
        po.setUpdatedAt(LocalDateTime.now());
        return toDomain(bookJpa.save(po));
    }

    @Transactional
    @Override
    public void delete(Long id, Long requesterId) {
        bookJpa.deleteById(id);
    }

    @Transactional
    @Override
    public void like(Long bookId, Long userId) {
        var book = bookJpa.findById(bookId).orElseThrow();
        var user = userJpa.findById(userId).orElseThrow();
        if (!likeJpa.existsByUser_IdAndBook_Id(userId, bookId)) {
            likeJpa.save(BookLikePO.of(book, user, LocalDateTime.now())); // ✅ 用工厂方法设置 EmbeddedId
            book.setLikesCount(book.getLikesCount() + 1);
            bookJpa.save(book);
        }
    }

    @Transactional
    @Override
    public void unlike(Long bookId, Long userId) {
        var book = bookJpa.findById(bookId).orElseThrow();
        if (likeJpa.existsByUser_IdAndBook_Id(userId, bookId)) {
            likeJpa.deleteByUser_IdAndBook_Id(userId, bookId);
            book.setLikesCount(Math.max(0, book.getLikesCount() - 1));
            bookJpa.save(book);
        }
    }

    @Transactional
    @Override
    public void bookmark(Long bookId, Long userId) {
        var book = bookJpa.findById(bookId).orElseThrow();
        var user = userJpa.findById(userId).orElseThrow();
        if (!bookmarkJpa.existsByUser_IdAndBook_Id(userId, bookId)) {
            bookmarkJpa.save(BookBookmarkPO.of(book, user, LocalDateTime.now())); // ✅ 用工厂方法设置 EmbeddedId
            book.setBookmarksCount(book.getBookmarksCount() + 1);
            bookJpa.save(book);
        }
    }

    @Transactional
    @Override
    public void unbookmark(Long bookId, Long userId) {
        var book = bookJpa.findById(bookId).orElseThrow();
        if (bookmarkJpa.existsByUser_IdAndBook_Id(userId, bookId)) {
            bookmarkJpa.deleteByUser_IdAndBook_Id(userId, bookId);
            book.setBookmarksCount(Math.max(0, book.getBookmarksCount() - 1));
            bookJpa.save(book);
        }
    }

    @Transactional
    @Override
    public Comment addComment(Long bookId, Long userId, String text, Long parentId) {
        var book = bookJpa.findById(bookId).orElseThrow();
        var user = userJpa.findById(userId).orElseThrow();
        CommentPO parent = null;
        if (parentId != null) {
            parent = commentJpa.findById(parentId).orElseThrow();
            parent.setRepliesCount(parent.getRepliesCount() + 1);
            commentJpa.save(parent);
        }
        var c = commentJpa.save(CommentPO.builder()
                .book(book)
                .user(user)
                .content(text)
                .parent(parent)
                .likesCount(0)
                .repliesCount(0)
                .createdAt(LocalDateTime.now()).build());
        book.setComments(book.getComments() + 1);
        bookJpa.save(book);

        return toDomainComment(c, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Comment> comments(Long bookId, Long userId, int page, int size) {
        var p = commentJpa.findByBook_IdAndParentIsNullOrderByCreatedAtDesc(bookId, PageRequest.of(page - 1, size));
        return p.map(c -> toDomainComment(c, userId));
    }

    @Transactional
    @Override
    public Comment likeComment(Long commentId, Long userId) {
        var comment = commentJpa.findById(commentId).orElseThrow();
        var user = userJpa.findById(userId).orElseThrow();
        if (!commentLikeJpa.existsByComment_IdAndUser_Id(commentId, userId)) {
            commentLikeJpa.save(CommentLikePO.builder().comment(comment).user(user).createdAt(LocalDateTime.now()).build());
            comment.setLikesCount(comment.getLikesCount() + 1);
            commentJpa.save(comment);
        }
        return toDomainComment(comment, userId);
    }

    @Transactional
    @Override
    public Comment unlikeComment(Long commentId, Long userId) {
        var comment = commentJpa.findById(commentId).orElseThrow();
        if (commentLikeJpa.existsByComment_IdAndUser_Id(commentId, userId)) {
            commentLikeJpa.deleteByComment_IdAndUser_Id(commentId, userId);
            comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
            commentJpa.save(comment);
        }
        return toDomainComment(comment, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment findComment(Long commentId, Long userId) {
        var c = commentJpa.findById(commentId).orElseThrow();
        return toDomainComment(c, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByTitleAndAuthor(String title, String author) {
        return bookJpa.existsByTitleAndAuthor(title, author);
    }

    private Comment toDomainComment(CommentPO po, Long userId) {
        boolean liked = userId != null && commentLikeJpa.existsByComment_IdAndUser_Id(po.getId(), userId);
        List<Comment> replies = commentJpa.findByParent_IdOrderByCreatedAtAsc(po.getId()).stream()
                .map(r -> toDomainComment(r, userId)).collect(Collectors.toList());
        return Comment.builder()
                .id(po.getId())
                .userId(po.getUser().getId()).userName(po.getUser().getNick()).userAvatar(po.getUser().getAvatar())
                .text(po.getContent())
                .parentId(po.getParent() != null ? po.getParent().getId() : null)
                .likes(po.getLikesCount())
                .liked(liked)
                .repliesCount(po.getRepliesCount())
                .replies(replies)
                .createdAt(po.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
                .build();
    }

    private Book toDomain(BookPO po) {
        var tags = po.getTags().stream().map(TagPO::getName).sorted().toList();
        var created = po.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant();
        var editableUntil = po.getCreatedAt().plusHours(24)
                .atZone(java.time.ZoneId.systemDefault()).toInstant();

        return Book.builder()
                .id(po.getId()).title(po.getTitle()).author(po.getAuthor()).orientation(po.getOrientation()).category(po.getCategory())
                .blurb(po.getBlurb()).summary(po.getSummary()).coverUrl(po.getCoverUrl())
                .createdAt(created).editableUntil(editableUntil)
                .likes(po.getLikesCount()).bookmarks(po.getBookmarksCount()).comments(po.getComments())
                .recommender(Book.Recommender.builder()
                        .id(po.getRecommender().getId())
                        .name(po.getRecommender().getNick())
                        .avatar(po.getRecommender().getAvatar()).build())
                .tags(tags)
                .build();
    }
}
