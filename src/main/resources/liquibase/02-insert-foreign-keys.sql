--liquibase formatted sql
--changeset forum-app:1
ALTER TABLE users
    ADD (report_id bigint DEFAULT NULL,
         role_id bigint DEFAULT NULL,
         KEY FKn98p61o06sjqh24ukx725frcs (report_id),
         KEY FK7smar58ju7p46pc8bvtb8qfp7 (role_id),
         CONSTRAINT FK7smar58ju7p46pc8bvtb8qfp7 FOREIGN KEY (role_id) REFERENCES role (id),
         CONSTRAINT FKn98p61o06sjqh24ukx725frcs FOREIGN KEY (report_id) REFERENCES report (id)
        );

--changeset forum-app:2
ALTER TABLE topic
    ADD (category_id bigint DEFAULT NULL,
         user_id bigint DEFAULT NULL,
         KEY FK8n7r9utm8sjpdfstb4wcqd7qj (category_id),
         KEY FK3qd4k7we4cdll00i6x84afhi8 (user_id),
         CONSTRAINT FK3qd4k7we4cdll00i6x84afhi8 FOREIGN KEY (user_id) REFERENCES users (id),
         CONSTRAINT FK8n7r9utm8sjpdfstb4wcqd7qj FOREIGN KEY (category_id) REFERENCES category (id)
        );

--changeset forum-app:3
ALTER TABLE comment
    ADD (topic_id bigint DEFAULT NULL,
         user_id bigint DEFAULT NULL,
         KEY FKo3bvevu9ua4w6f8qu2b177f16 (topic_id),
         KEY FKqm52p1v3o13hy268he0wcngr5 (user_id),
         CONSTRAINT FKo3bvevu9ua4w6f8qu2b177f16 FOREIGN KEY (topic_id) REFERENCES topic (id),
         CONSTRAINT FKqm52p1v3o13hy268he0wcngr5 FOREIGN KEY (user_id) REFERENCES users (id)
        );

--changeset forum-app:4
ALTER TABLE likes
    ADD (comment_id bigint DEFAULT NULL,
         topic_id bigint DEFAULT NULL,
         user_id bigint DEFAULT NULL,
         KEY FK8arpx7i3g3e5dammtdsira2m6 (comment_id),
         KEY FK8v0t6mcj0gt1lfu2n7w1ck1ry (topic_id),
         KEY FKnvx9seeqqyy71bij291pwiwrg (user_id),
         CONSTRAINT FK8arpx7i3g3e5dammtdsira2m6 FOREIGN KEY (comment_id) REFERENCES comment (id),
         CONSTRAINT FK8v0t6mcj0gt1lfu2n7w1ck1ry FOREIGN KEY (topic_id) REFERENCES topic (id),
         CONSTRAINT FKnvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES users (id)
         );

--changeset forum-app:5
ALTER TABLE report
    ADD (comment_id bigint DEFAULT NULL,
         topic_id bigint DEFAULT NULL,
         user_id bigint DEFAULT NULL,
         KEY FK1dw2gwqqspkllnye2ylaiabqx (comment_id),
         KEY FKh1fqnm6tj4se3bltd2p65tigw (topic_id),
         KEY FKn98p61o06sjqh24ukx725ffff (user_id),
         CONSTRAINT FK1dw2gwqqspkllnye2ylaiabqx FOREIGN KEY (comment_id) REFERENCES comment (id),
         CONSTRAINT FKh1fqnm6tj4se3bltd2p65tigw FOREIGN KEY (topic_id) REFERENCES topic (id),
         CONSTRAINT FKn98p61o06sjqh24ukx725ffff FOREIGN KEY (user_id) REFERENCES users (id)
         );

--changeset forum-app:6
ALTER TABLE warning
    ADD (user_id bigint DEFAULT NULL,
         KEY FKnre58jl6dwfb9qh5udmdl5sl3 (user_id),
         CONSTRAINT FKnre58jl6dwfb9qh5udmdl5sl3 FOREIGN KEY (user_id) REFERENCES users (id)
         );