--liquibase formatted sql
--changeset forum-app:1
CREATE TABLE category (
                          id bigint NOT NULL AUTO_INCREMENT,
                          description varchar(200) DEFAULT NULL,
                          title varchar(50) DEFAULT NULL,
                          PRIMARY KEY (id),
                          UNIQUE KEY UK_lnmf77qvjnr2lmyxrrydom9hd (title)
) ENGINE=InnoDB;

--changeset forum-app:5
CREATE TABLE role (
                      id bigint NOT NULL AUTO_INCREMENT,
                      name varchar(255) DEFAULT NULL,
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

--changeset forum-app:7
CREATE TABLE users (
                       id bigint NOT NULL AUTO_INCREMENT,
                       blocked bit(1) NOT NULL,
                       created_at datetime(6) DEFAULT NULL,
                       last_activity datetime(6) DEFAULT NULL,
                       password varchar(255) DEFAULT NULL,
                       username varchar(255) DEFAULT NULL,
--                        report_id bigint DEFAULT NULL,
--                        role_id bigint DEFAULT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK_r43af9ap4edm43mmtq01oddj6 (username)
--                        KEY FKn98p61o06sjqh24ukx725frcs (report_id),
--                        KEY FK7smar58ju7p46pc8bvtb8qfp7 (role_id),
--                        CONSTRAINT FK7smar58ju7p46pc8bvtb8qfp7 FOREIGN KEY (role_id) REFERENCES role (id),
--                        CONSTRAINT FKn98p61o06sjqh24ukx725frcs FOREIGN KEY (report_id) REFERENCES report (id)
) ENGINE=InnoDB;

--changeset forum-app:6
CREATE TABLE topic (
                       id bigint NOT NULL AUTO_INCREMENT,
                       closed bit(1) NOT NULL,
                       content varchar(255) DEFAULT NULL,
                       created_at datetime(6) DEFAULT NULL,
                       last_edit datetime(6) DEFAULT NULL,
                       title varchar(255) DEFAULT NULL,
--                        category_id bigint DEFAULT NULL,
--                        user_id bigint DEFAULT NULL,
                       PRIMARY KEY (id)
--                        KEY FK8n7r9utm8sjpdfstb4wcqd7qj (category_id),
--                        KEY FK3qd4k7we4cdll00i6x84afhi8 (user_id),
--                        CONSTRAINT FK3qd4k7we4cdll00i6x84afhi8 FOREIGN KEY (user_id) REFERENCES users (id),
--                        CONSTRAINT FK8n7r9utm8sjpdfstb4wcqd7qj FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE=InnoDB;

--changeset forum-app:2
CREATE TABLE comment (
                         id bigint NOT NULL AUTO_INCREMENT,
                         content varchar(255) DEFAULT NULL,
                         created_at datetime(6) DEFAULT NULL,
                         last_edit datetime(6) DEFAULT NULL,
--                          topic_id bigint DEFAULT NULL,
--                          user_id bigint DEFAULT NULL,
                         PRIMARY KEY (id)
--                          KEY FKo3bvevu9ua4w6f8qu2b177f16 (topic_id),
--                          KEY FKqm52p1v3o13hy268he0wcngr5 (user_id),
--                          CONSTRAINT FKo3bvevu9ua4w6f8qu2b177f16 FOREIGN KEY (topic_id) REFERENCES topic (id),
--                          CONSTRAINT FKqm52p1v3o13hy268he0wcngr5 FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;

--changeset forum-app:3
CREATE TABLE likes (
                       id bigint NOT NULL AUTO_INCREMENT,
--                        comment_id bigint DEFAULT NULL,
--                        topic_id bigint DEFAULT NULL,
--                        user_id bigint DEFAULT NULL,
                       PRIMARY KEY (id)
--                        KEY FK8arpx7i3g3e5dammtdsira2m6 (comment_id),
--                        KEY FK8v0t6mcj0gt1lfu2n7w1ck1ry (topic_id),
--                        KEY FKnvx9seeqqyy71bij291pwiwrg (user_id),
--                        CONSTRAINT FK8arpx7i3g3e5dammtdsira2m6 FOREIGN KEY (comment_id) REFERENCES comment (id),
--                        CONSTRAINT FK8v0t6mcj0gt1lfu2n7w1ck1ry FOREIGN KEY (topic_id) REFERENCES topic (id),
--                        CONSTRAINT FKnvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;

--changeset forum-app:4
CREATE TABLE report (
                        id bigint NOT NULL AUTO_INCREMENT,
                        created_at datetime(6) DEFAULT NULL,
                        reason varchar(255) DEFAULT NULL,
                        seen bit(1) NOT NULL,
--                         comment_id bigint DEFAULT NULL,
--                         topic_id bigint DEFAULT NULL,
--                         user_report varchar(255) DEFAULT NULL,
                        PRIMARY KEY (id)
--                         KEY FK1dw2gwqqspkllnye2ylaiabqx (comment_id),
--                         KEY FKh1fqnm6tj4se3bltd2p65tigw (topic_id),
--                         KEY FKp8qp6pgibqtgfdgge1mhnhpmj (user_report),
--                         CONSTRAINT FK1dw2gwqqspkllnye2ylaiabqx FOREIGN KEY (comment_id) REFERENCES comment (id),
--                         CONSTRAINT FKh1fqnm6tj4se3bltd2p65tigw FOREIGN KEY (topic_id) REFERENCES topic (id),
--                         CONSTRAINT FKp8qp6pgibqtgfdgge1mhnhpmj FOREIGN KEY (user_report) REFERENCES users (username)
) ENGINE=InnoDB;

--changeset forum-app:8
CREATE TABLE warning (
                         id bigint NOT NULL AUTO_INCREMENT,
--                          user_id bigint DEFAULT NULL,
                         PRIMARY KEY (id)
--                          KEY FKnre58jl6dwfb9qh5udmdl5sl3 (user_id),
--                          CONSTRAINT FKnre58jl6dwfb9qh5udmdl5sl3 FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;