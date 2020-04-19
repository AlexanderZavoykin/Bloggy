INSERT INTO users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000001, 'first@test.com', false, 'Unactivated test user', 'DonaldDuck',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000002, 'second@test.com', true, 'Activated test user', 'CharlieSheen',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000003, 'third@test.com', true, 'Admin, not user', 'HolyAdmin',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO user_role (user_id, roles)
VALUES (1000001, 'USER');

INSERT INTO user_role (user_id, roles)
VALUES (1000002, 'USER');

INSERT INTO user_role (user_id, roles)
VALUES (1000003, 'ADMIN');

INSERT INTO story
    (story_id, body, created, rough, title, updated, user_id)
VALUES (1000001, 'Just some test story text', '2020-03-31 12:31:38.334', false, 'Oh what a day in LA',
        '2020-03-31 12:31:38.334', 1000001);

INSERT INTO story
    (story_id, body, created, rough, title, updated, user_id)
VALUES (1000002, 'Just another story', '2020-03-31 12:31:38.334', false, 'How I spend summer',
        '2020-03-31 12:31:38.334', 1000002);

INSERT INTO story
    (story_id, body, created, rough, title, updated, user_id)
VALUES (1000003, 'Rough story - not for publishing yet', '2020-03-31 12:31:38.334', true, 'What about ice-cream?',
        '2020-03-31 12:31:38.334', 1000002);

INSERT INTO tag
    (tag_id, created, name)
VALUES (1000101, '2020-03-31 12:03:45.931', 'Java');

INSERT INTO tag
    (tag_id, created, name)
VALUES (1000102, '2020-03-31 12:03:45.931', 'Travel');

INSERT INTO tag
    (tag_id, created, name)
VALUES (1000103, '2020-03-31 12:03:45.931', 'King Kong');

INSERT INTO story_tag
    (story_id, tag_id)
VALUES (1000001, 1000101);

INSERT INTO story_tag
    (story_id, tag_id)
VALUES (1000001, 1000102);

INSERT INTO story_tag
    (story_id, tag_id)
VALUES (1000002, 1000102);

INSERT INTO story_tag
    (story_id, tag_id)
VALUES (1000003, 1000103);

INSERT INTO comment
    (comment_id, body, created, user_id, story_id)
VALUES (1000001, 'It`s awesome!', '2020-03-31 13:22:45.177', 1000002, 1000002);

