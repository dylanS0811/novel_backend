-- Ensure uniqueness of books within a list based on content signature
CREATE UNIQUE INDEX uk_book_list_books_signature
    ON book_list_books (book_list_id, title, author, orientation, category);
