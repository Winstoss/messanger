CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS usr
(
    id              UUID            DEFAULT uuid_generate_v4(),
    nickname        VARCHAR(32)     NOT NULL,
    username        VARCHAR(32)     NOT NULL UNIQUE,
    password        VARCHAR(32)     NOT NULL,
    phone_number    VARCHAR(16)     DEFAULT NULL,
    bio             VARCHAR(256)    DEFAULT NULL,
    avatar_path     VARCHAR(64)     DEFAULT '/images/image1.jpg',
    created_at      TIMESTAMP       DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT unique_username_constraint UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS group_chat
(
    id              UUID            DEFAULT uuid_generate_v4(),
    creator_id      UUID,
    title           VARCHAR(32)     NOT NULL,
    image_path      VARCHAR(64)     DEFAULT '/images/image1.jpg',
    description     VARCHAR(512)    DEFAULT NULL,
    created_at      TIMESTAMP       DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT creator_id_constraint FOREIGN KEY (creator_id) REFERENCES usr (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS private_chat
(
    id              UUID            DEFAULT uuid_generate_v4(),
    first_user_id   UUID,
    second_user_id  UUID,
    created_at      TIMESTAMP       DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT  first_user_id_constraint FOREIGN KEY (first_user_id) REFERENCES usr (id) ON DELETE SET NULL,
    CONSTRAINT  second_user_id_constraint FOREIGN KEY (second_user_id) REFERENCES usr (id) ON DELETE SET NULL,
    CONSTRAINT unique_private_chat_constraint UNIQUE (first_user_id, second_user_id)
);

CREATE TABLE IF NOT EXISTS private_message
(
    id              UUID            DEFAULT uuid_generate_v4(),
    chat_id         UUID,
    author_id       UUID,
    content         VARCHAR(2048)   NOT NULL,
    seen            BOOLEAN         DEFAULT false,
    created_at      TIMESTAMP       DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT author_id_constraint FOREIGN KEY (author_id) REFERENCES usr (id) ON DELETE SET NULL,
    CONSTRAINT chat_id_constraint FOREIGN KEY (chat_id) REFERENCES private_chat (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS group_message
(
    id              UUID            DEFAULT uuid_generate_v4(),
    chat_id         UUID,
    author_id       UUID,
    content         VARCHAR(2048)   NOT NULL,
    seen            BOOLEAN         DEFAULT false,
    created_at      TIMESTAMP       DEFAULT NOW(),

    PRIMARY KEY (id),
    CONSTRAINT author_id_constraint FOREIGN KEY (author_id) REFERENCES usr (id) ON DELETE SET NULL,
    CONSTRAINT chat_id_constraint FOREIGN KEY (chat_id) REFERENCES group_chat (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS group_chat_admin
(
    id              UUID    DEFAULT uuid_generate_v4(),
    chat_id         UUID    NOT NULL,
    admin_id        UUID    NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT chat_id_constraint FOREIGN KEY (chat_id) REFERENCES group_chat (id) ON DELETE CASCADE ,
    CONSTRAINT admin_id_constraint FOREIGN KEY (admin_id) REFERENCES usr (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS group_chat_user
(
    id              UUID    DEFAULT uuid_generate_v4(),
    chat_id         UUID    NOT NULL,
    user_id         UUID    NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT chat_id_constraint FOREIGN KEY (chat_id) REFERENCES group_chat (id) ON DELETE CASCADE,
    CONSTRAINT user_id_constraint FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE
);

