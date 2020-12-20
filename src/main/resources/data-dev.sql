-- User
insert into user(username, password) values
('user', '$2a$10$8uWwUAEEZ66bcQKzUN6HfeYMMmYCLmTrsHhzmJfDTCphBsiv/bGEe');

-- Inbound
insert into inbound(id, host, username, password) values
(1, 'mail1.test', 'user1', 'password'),
(2, 'mail1.test', 'user2', 'password'),
(3, 'mail2.test', 'user1', 'password');

-- Account
insert into account(id, name, color, user_username, inbound_id) values
(1, 'account1', 1, 'user', 1),
(2, 'account2', 2, 'user', 2),
(3, 'account3', 3, 'user', 3);
