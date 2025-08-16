ALTER TABLE book ADD CONSTRAINT uq_book_title_author UNIQUE (title, author);
ALTER TABLE user ADD CONSTRAINT uq_user_nick UNIQUE (nick);
