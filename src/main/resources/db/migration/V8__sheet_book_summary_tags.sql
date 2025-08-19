ALTER TABLE book_list_books ADD COLUMN summary TEXT;

CREATE TABLE IF NOT EXISTS book_list_book_tags (
    book_list_book_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (book_list_book_id, tag_id),
    CONSTRAINT fk_blbt_book FOREIGN KEY (book_list_book_id) REFERENCES book_list_books(id) ON DELETE CASCADE,
    CONSTRAINT fk_blbt_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
