INSERT INTO role (id, name, code) VALUES (1, 'ROLE_USER', '123');
INSERT INTO role (id, name, code) VALUES (2, 'ROLE_MODERATOR', '123');
INSERT INTO role (id, name, code) VALUES (3, 'ROLE_ADMIN', '123');

INSERT INTO users (id, username, password, blocked, role_name) VALUES (1, 'userJwt', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (2, 'adminJwt', 'encrypted_password', false, 'ROLE_ADMIN');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (3, 'moderatorJwt', 'encrypted_password', false, 'ROLE_MODERATOR');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (4, 'user', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (5, 'userToChangePassword', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (6, 'userToUnban', 'encrypted_password', true, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (7, 'userToCheckUnban', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (8, 'userToBan', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (9, 'userToCheckBan', 'encrypted_password', true, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (10, 'userToDelete', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (11, 'userToDeleteByAdmin', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (12, 'userToChangeRole', 'encrypted_password', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (13, 'userWithSameRole', 'encrypted_password', false, 'ROLE_USER');