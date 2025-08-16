INSERT INTO user(nick, avatar)
VALUES ('麦穗', 'https://example.com/a1.png');
INSERT INTO user(nick, avatar)
VALUES ('谷雨', 'https://example.com/a2.png');
INSERT INTO tag(name)
VALUES ('科幻'),
       ('群像'),
       ('慢热'),
       ('现言');

INSERT INTO book(title, author, orientation, category, blurb, summary, cover_url, recommender_id, likes_count,
                 bookmarks_count, comments_count)
VALUES ('《星海折雪》', '秣丹', 'BL主受', '科幻', '一句话强推', '一段简介', 'https://example.com/c1.jpg', 1, 10, 5, 2);
INSERT INTO book_tag(book_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

INSERT INTO notification(user_id, type, title, content, is_read)
VALUES (1, 'system', '欢迎', '欢迎来到书粮社区', 0);
