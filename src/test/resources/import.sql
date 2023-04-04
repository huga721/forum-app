INSERT INTO role (id, name, code) VALUES (1, 'ROLE_USER', '123');
INSERT INTO role (id, name, code) VALUES (2, 'ROLE_MODERATOR', '123');
INSERT INTO role (id, name, code) VALUES (3, 'ROLE_ADMIN', '123');

INSERT INTO users (id, username, password, blocked, role_name) VALUES (1, 'userJwt', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (2, 'adminJwt', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_ADMIN');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (3, 'moderatorJwt', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_MODERATOR');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (4, 'user', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (5, 'userToChangePassword', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (6, 'userToUnban', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', true, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (7, 'userToCheckUnban', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (8, 'userToBan', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (9, 'userToCheckBan', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', true, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (10, 'userToDelete', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (11, 'userToDeleteByAdmin', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (12, 'userToChangeRole', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (13, 'userWithSameRole', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', false, 'ROLE_USER');
INSERT INTO users (id, username, password, blocked, role_name) VALUES (14, 'userBanned', '$2a$12$pXbWi0cGMnzxOddlT6RIGOYYGt9/IAdLU9Gkzeyvc13tudavD2RV2', true, 'ROLE_USER');

INSERT INTO category (id, title, description) VALUES (1, 'test title', 'test description');
INSERT INTO category (id, title, description) VALUES (2, 'second test title', 'second test description');
INSERT INTO category (id, title, description) VALUES (3, 'category to change title', 'lorem ipsum ipsum lorem');
INSERT INTO category (id, title, description) VALUES (4, 'category for throw test', 'throw it');
INSERT INTO category (id, title, description) VALUES (5, 'category to delete', 'delete it');
INSERT INTO category (id, title, description) VALUES (6, 'category for topic', 'nice category');

INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (1, 'first test topic', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (2, 'second test topic', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (3, 'topic duplicate', 'random test content', 2,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (4, 'topic to delete by author', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (5, 'topic to delete by author but throw exception', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (6, 'topic to close by author', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (7, 'topic to edit by author', 'random test content', 1,1, false);
INSERT INTO topic (id, title, content, category_id, user_id, closed) VALUES (8, 'closed topic', 'random test content', 1,1, true);
-- INSERT INTO topic (id, title, content, category_id, user_id) VALUES (3, 'test for topic with same title but in different category', 'random test content', 1,1);
-- INSERT INTO topic (id, title, content) VALUES (1, 'userJwt', 'encrypted_password');
