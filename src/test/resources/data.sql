INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000001, 'first@test.com', false, 'Unactivated test user', 'DonaldDuck',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000002, 'second@test.com', true, 'Activated test user', 'CharlieSheen',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');

INSERT INTO public.users (user_id, email, enabled, info, nickname, password, registered)
VALUES (1000003, 'third@test.com', true, 'Admin, not user', 'HolyAdmin',
        '$2a$10$4DXMq/1PI7mcGuTfuWycJO8BNpsok.PBn2AjANQ6TifLTdb08z.Pu', '2020-03-30 15:03:40.418');


INSERT INTO public.user_role (user_id, roles)
VALUES (1000001, 'USER');

INSERT INTO public.user_role (user_id, roles)
VALUES (1000002, 'USER');

INSERT INTO public.user_role (user_id, roles)
VALUES (1000003, 'ADMIN');

