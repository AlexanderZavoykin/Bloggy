INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000001, 'unactivated@test.com', false, 'Unactivated test user', 'Unactivated',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000002, 'activated@test.com', true, 'Activated test user', 'Activated',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000003, 'admin@test.com', true, 'Admin', 'Admin test user',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');


INSERT INTO public.user_role (user_id, roles)
VALUES (1000001, 'USER');

INSERT INTO public.user_role (user_id, roles)
VALUES (1000002, 'USER');

INSERT INTO public.user_role (user_id, roles)
VALUES (1000003, 'ADMIN');

