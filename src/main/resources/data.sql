insert into portal_user_role values (1, 'ROLE_ADMIN');
insert into portal_user_role values (2, 'ROLE_USER');

insert into portal_user (id, full_name, username, password, jira_basic_token)
values (1, 'Admin', 'admin', '$2a$11$MVep13x4Q5AVki5/ZaOy9uo9FNtc7ZTpo.Zo5IV6BG.Ar/TXq6iTK', 'jira_basic_token');
insert into portal_user (id, full_name, username, password, jira_basic_token)
values (2, 'User', 'user',   '$2a$11$MVep13x4Q5AVki5/ZaOy9uo9FNtc7ZTpo.Zo5IV6BG.Ar/TXq6iTK', 'jira_basic_token');

insert into portal_user_portal_user_role values (1, 1);
insert into portal_user_portal_user_role values (2, 2);