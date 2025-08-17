CREATE TABLE IF NOT EXISTS user
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) UNIQUE,
    email VARCHAR(40) UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    nick VARCHAR(40) NOT NULL,
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tag
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    name
    VARCHAR
(
    40
) UNIQUE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS book
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    title
    VARCHAR
(
    120
) NOT NULL,
    author VARCHAR
(
    80
),
    orientation VARCHAR
(
    20
) NOT NULL,
    category VARCHAR
(
    20
) NOT NULL,
    blurb VARCHAR
(
    200
),
    summary TEXT,
    cover_url VARCHAR
(
    255
),
    recommender_id BIGINT NOT NULL,
    likes_count INT NOT NULL DEFAULT 0,
    bookmarks_count INT NOT NULL DEFAULT 0,
    comments_count INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_book_category
(
    category
),
    INDEX idx_book_orientation
(
    orientation
),
    CONSTRAINT fk_book_user FOREIGN KEY
(
    recommender_id
) REFERENCES user
(
    id
)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS book_tag
(
    book_id
    BIGINT
    NOT
    NULL,
    tag_id
    BIGINT
    NOT
    NULL,
    PRIMARY
    KEY
(
    book_id,
    tag_id
),
    FOREIGN KEY
(
    book_id
) REFERENCES book
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    tag_id
) REFERENCES tag
(
    id
)
  ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS book_like
(
    book_id
    BIGINT
    NOT
    NULL,
    user_id
    BIGINT
    NOT
    NULL,
    created_at
    DATETIME
    NOT
    NULL
    DEFAULT
    CURRENT_TIMESTAMP,
    PRIMARY
    KEY
(
    book_id,
    user_id
),
    FOREIGN KEY
(
    book_id
) REFERENCES book
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    user_id
) REFERENCES user
(
    id
)
  ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS book_bookmark
(
    book_id
    BIGINT
    NOT
    NULL,
    user_id
    BIGINT
    NOT
    NULL,
    created_at
    DATETIME
    NOT
    NULL
    DEFAULT
    CURRENT_TIMESTAMP,
    PRIMARY
    KEY
(
    book_id,
    user_id
),
    FOREIGN KEY
(
    book_id
) REFERENCES book
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    user_id
) REFERENCES user
(
    id
)
  ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comment
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    book_id
    BIGINT
    NOT
    NULL,
    user_id
    BIGINT
    NOT
    NULL,
    content
    VARCHAR
(
    500
) NOT NULL,
    parent_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY
(
    book_id
) REFERENCES book
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    user_id
) REFERENCES user
(
    id
)
  ON DELETE CASCADE,
    FOREIGN KEY
(
    parent_id
) REFERENCES comment
(
    id
)
  ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notification
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    user_id
    BIGINT
    NOT
    NULL,
    type
    VARCHAR
(
    20
) NOT NULL,
    title VARCHAR
(
    120
),
    content VARCHAR
(
    255
),
    actor_id BIGINT,
    book_id BIGINT,
    comment_id BIGINT,
    is_read TINYINT
(
    1
) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY
(
    user_id
) REFERENCES user
(
    id
) ON DELETE CASCADE,
    FOREIGN KEY
(
    actor_id
) REFERENCES user
(
    id
)
  ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
