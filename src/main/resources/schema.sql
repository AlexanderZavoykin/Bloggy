CREATE SEQUENCE IF NOT EXISTS user_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS user_token_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS story_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS login_attempt_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS tag_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS comment_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS users
(
    user_id    int8         NOT NULL DEFAULT user_seq.nextval,
    email      varchar(255) NOT NULL,
    enabled    bool         NOT NULL,
    info       varchar(255),
    nickname   varchar(20)  NOT NULL,
    password   varchar(60)  NOT NULL,
    registered timestamp    NOT NULL DEFAULT now(),
    CONSTRAINT email_ui UNIQUE (email),
    CONSTRAINT nickname_ui UNIQUE (nickname),
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id int8         NOT NULL,
    roles   varchar(255) NOT NULL,
    CONSTRAINT role_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_token
(
    user_token_id int8 DEFAULT user_token_seq.nextval,
    expiry_date   timestamp   NOT NULL,
    token         varchar(36) NOT NULL,
    user_id       int8        NOT NULL,
    CONSTRAINT user_id_ui UNIQUE (user_id),
    CONSTRAINT user_token_pkey PRIMARY KEY (user_token_id),
    CONSTRAINT user_token_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS login_attempt
(
    login_attempt_id int8               DEFAULT login_attempt_seq.nextval,
    attempted        timestamp NOT NULL DEFAULT now(),
    success          bool      NOT NULL,
    user_id          int8      NOT NULL,
    CONSTRAINT login_attempt_pkey PRIMARY KEY (login_attempt_id),
    CONSTRAINT login_attempt_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS story
(
    story_id int8                  DEFAULT story_seq.nextval,
    body     text         NOT NULL,
    created  timestamp    NOT NULL DEFAULT now(),
    rough    bool                  DEFAULT false,
    title    varchar(255) NOT NULL,
    updated  timestamp    NOT NULL DEFAULT now(),
    user_id  int8         NOT NULL,
    CONSTRAINT story_pkey PRIMARY KEY (story_id),
    CONSTRAINT story_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS tag
(
    tag_id  int8                 DEFAULT tag_seq.nextval,
    created timestamp   NOT NULL DEFAULT now(),
    name    varchar(30) NOT NULL,
    CONSTRAINT name_ui UNIQUE (name),
    CONSTRAINT tag_pkey PRIMARY KEY (tag_id)
);

CREATE TABLE IF NOT EXISTS story_tag
(
    story_id int8 NOT NULL,
    tag_id   int8 NOT NULL,
    CONSTRAINT story_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES tag (tag_id),
    CONSTRAINT story_tag_story_fk FOREIGN KEY (story_id) REFERENCES story (story_id)
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_id int8                  DEFAULT comment_seq.nextval,
    body       varchar(255) NOT NULL,
    created    timestamp    NOT NULL DEFAULT now(),
    user_id    int8         NOT NULL,
    story_id   int8         NOT NULL,
    CONSTRAINT comment_pkey PRIMARY KEY (comment_id),
    CONSTRAINT comment_story_fk FOREIGN KEY (story_id) REFERENCES story (story_id),
    CONSTRAINT comment_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);