CREATE SEQUENCE public.user_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE public.user_token_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE public.story_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE public.login_attempt_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE public.tag_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE SEQUENCE public.comment_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1000000
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id    int8         NOT NULL,
    email      varchar(120) NOT NULL,
    enabled    bool         NOT NULL,
    info       varchar(255) NULL,
    nickname   varchar(20)  NOT NULL,
    "password" varchar(60)  NOT NULL,
    registered timestamp    NOT NULL,
    CONSTRAINT email_ui UNIQUE (email),
    CONSTRAINT nickname_ui UNIQUE (nickname),
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE public.user_role
(
    user_id int8         NOT NULL,
    roles   varchar(255) NULL,
    CONSTRAINT role_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE public.user_token
(
    user_token_id int8        NOT NULL,
    expiry_date   timestamp   NOT NULL,
    "token"       varchar(36) NOT NULL,
    user_id       int8        NOT NULL,
    CONSTRAINT user_id_ui UNIQUE (user_id),
    CONSTRAINT user_token_pkey PRIMARY KEY (user_token_id),
    CONSTRAINT user_token_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE public.login_attempt
(
    login_attempt_id int8      NOT NULL,
    attempted        timestamp NOT NULL,
    success          bool      NOT NULL,
    user_id          int8      NOT NULL,
    CONSTRAINT login_attempt_pkey PRIMARY KEY (login_attempt_id),
    CONSTRAINT login_attempt_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE public.story
(
    story_id int8         NOT NULL,
    body     text         NOT NULL,
    created  timestamp    NOT NULL,
    rough    bool         NULL DEFAULT false,
    title    varchar(255) NOT NULL,
    updated  timestamp    NOT NULL,
    user_id  int8         NULL,
    CONSTRAINT story_pkey PRIMARY KEY (story_id),
    CONSTRAINT story_user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE public.tag
(
    tag_id  int8        NOT NULL,
    created timestamp   NOT NULL,
    "name"  varchar(30) NOT NULL,
    CONSTRAINT name_ui UNIQUE (name),
    CONSTRAINT tag_pkey PRIMARY KEY (tag_id)
);

CREATE TABLE public.story_tag
(
    story_id int8 NOT NULL,
    tag_id   int8 NOT NULL,
    CONSTRAINT story_tag_tag_fk FOREIGN KEY (tag_id) REFERENCES tag (tag_id),
    CONSTRAINT story_tag_story_fk FOREIGN KEY (story_id) REFERENCES story (story_id)
);

CREATE TABLE public."comment"
(
    comment_id int8         NOT NULL,
    body       varchar(255) NOT NULL,
    created    timestamp    NOT NULL,
    user_id    int8         NULL,
    story_id   int8         NULL,
    CONSTRAINT comment_pkey PRIMARY KEY (comment_id),
    CONSTRAINT fkdj8brghihjo4r7eokvpb33s05 FOREIGN KEY (story_id) REFERENCES story (story_id),
    CONSTRAINT fkqm52p1v3o13hy268he0wcngr5 FOREIGN KEY (user_id) REFERENCES users (user_id)
);