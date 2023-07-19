--liquibase formatted sql
--changeset forum-app:insert-role-values
INSERT INTO role (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO role (id, name) VALUES (2, 'ROLE_MODERATOR');
INSERT INTO role (id, name) VALUES (3, 'ROLE_ADMIN');

--changeset forum-app:insert-user-values
INSERT INTO users (id, username, password, blocked, role_id)
VALUES (1, 'user', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (2, 'moderator', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 2);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (3, 'admin', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 3);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (4, 'Riddle', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (5, 'RequiredNickname', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (6, 'Pit Gull', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (7, 'friq1985', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (8, 'runnable', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (9, 'maxell', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (10, 'Bison', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (11, 'leonard', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (12, 'chacki', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (13, 'micky', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (14, 'Swimming pool', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

INSERT INTO users (id, username, password, blocked, role_id)
VALUES (15, 'workbuk', '$2a$10$tlGTeVlhY1Hn62relbFyiOazqpWDQ0GNrp9./PYJ1I7q9mU8CsAay', false, 1);

--changeset forum-app:insert-category-values
INSERT INTO category (id, title, description)
VALUES (1, 'Java', 'Discuss about Java');

INSERT INTO category (id, title, description)
VALUES (2, 'Python', 'Discuss about Python');

INSERT INTO category (id, title, description)
VALUES (3, 'PHP', 'Discuss about PHP');

INSERT INTO category (id, title, description)
VALUES (4, 'C# and .NET', 'Discuss about C# and .NET');

INSERT INTO category (id, title, description)
VALUES (5, 'JavaScript', 'Discuss about JavaScript');

INSERT INTO category (id, title, description)
VALUES (6, 'Other programming languages', 'Discuss about other programming languages');

INSERT INTO category (id, title, description)
VALUES (7, 'Databases', 'Discuss about databases');

INSERT INTO category (id, title, description)
VALUES (8, 'Dev/ops', 'Discuss about devops');

INSERT INTO category (id, title, description)
VALUES (9, 'Education', 'Discuss about education');

INSERT INTO category (id, title, description)
VALUES (10, 'Career', 'Discuss about career');

INSERT INTO category (id, title, description)
VALUES (11, 'Off-Topic', 'Discuss about off-topic');

--changeset forum-app:insert-topic-values
INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (1, 'Java imperative or declarative programming',
        'Whats the best programming method declarative or imperative?', false, 7, 1);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (2, 'Single Responsibility Principle(SRP) and my Service Class',
        'I have YoutubeVideoService class which does CRUD(Create, Read, Update, and Delete) operations. In
        my view Create, Read, Update, and Delete are four reasons for a class to change. Does this class
        violates Single Responsibility Principle?', false, 6, 1);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (3, 'Recommendation of books', 'I would like to read book about spring boot jpa, please give me some suggestions.',
        false, 4, 1);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (4, 'Python and ai', 'What should I learn next after Python to get a job with AI', false, 5, 2);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (5, 'Flask or Django', 'What has more perspective flask or django', false, 4, 2);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (6, 'Background logout', 'Hey. How I can make automatically logout in Laravel (if user is AFK for couple minutes then he should be logged out)',
        false, 7, 3);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (7, 'php frameworks', 'Whats the best PHP framework with the biggest job perspective', false, 8, 3);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (8, 'Interfaces - what is it and how to use it in C#', 'Like in title, I wonder whats your view on it',
        false, 6, 4);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (9, 'Macbook and programming in C#', 'Does anyone have experience in working with C# on macbook with m1 chip?',
        false, 10, 4);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (10, 'The best framework for learning java script', 'Please advise which js framework to start learning the frontend with? According to many sources, React is currently the most popular, while Vue is the easiest to start learning.',
        false, 9, 5);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (11, 'Which functional language', 'I would like to learn functional programming language but im wondering which', false, 10, 6);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (12, 'Deleting row', 'How to delete row in sql table?',
        false, 7, 7);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (13, 'Docker and macbook with m1 chip', 'What is the current Docker situation on the M1 mac? Is it working? causes problems?',
        false, 9, 8);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (14, 'Books', 'What IT related book would you recommend', false, 4, 9);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (15, 'How much do you earn?', 'Due to many questions about earning im creating topic and whoever want to write about earning can do it here. I will start 4 y exp 20k pln software engineer tech .net',
        false, 5, 10);

INSERT INTO topic (id, title, content, closed, user_id, category_id)
VALUES (16, 'Burnout at the beginning of career', 'One year as software developer, during IT bachelor studies and I feel like im already burnout. How do you deal with burnout?',
        false, 6, 11);

--changeset forum-app:insert-comment-values
INSERT INTO comment (id, content, topic_id, user_id)
VALUES (1, 'Personally I think declarative programming in Java is more elegant and readable than imperative approach. It allows you to focus on declaration of intent and data manipulation, not on implementation details',
        1, 10);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (2, 'Of course, declarative programming in Java is future! This approach makes the code more modular, easier to maintain and test. Imperative programming can lead to bugs and loss of code clarity.',
        1, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (3, 'I guess it depends of the context and the type of project. Sometimes imperative approach may be more appropriate especially for simple algorithms or manipulation of low-level operations.',
        1, 9);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (4, 'Yes, it violates SRP because it handles multiple tasks: Create, Read, Update, and Delete.', 2, 10);


INSERT INTO comment (id, content, topic_id, user_id)
VALUES (5, 'Agreed, it violates SRP. Separate the responsibilities into different classes for better maintainability.',
        2, 4);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (6, 'It depends on the context, but separating the responsibilities could improve future changes and maintenance.',
        2, 5);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (7, 'The class has multiple responsibilities. Consider separating them for better code structure.',
        2, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (8, 'It violates SRP. Refactor by creating separate classes for each operation.', 2, 9);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (9, 'Check out "Pro Spring Boot 2" by Felipe Gutierrez. It covers Spring Boot and JPA integration with practical examples. Great for learning Spring Boot JPA.',
        3, 10);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (10, '"Spring Data JPA: Learn Springs Easiest Data Access Abstraction" by Petri Kainulainen is a focused resource on Spring Boot JPA. It provides clear explanations and practical insights.',
        3, 4);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (11, '"Spring Boot in Action" by Craig Walls is a comprehensive book on Spring Boot, including JPA integration. It offers practical examples and best practices for Spring Boot and JPA.',
        3, 6);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (12, 'Learn machine learning frameworks like TensorFlow or PyTorch. Also, gain knowledge in data analysis, statistics, and algorithms to strengthen your AI skillset.',
        4, 7);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (13, 'Focus on data science concepts and tools. Learn libraries like NumPy and pandas. Gain knowledge in machine learning, natural language processing, and computer vision.',
        4, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (14, 'Focus on data science concepts and tools. Learn libraries like NumPy and pandas. Gain knowledge in machine learning, natural language processing, and computer vision.',
        4, 10);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (15, 'Flask and Django have different perspectives. Flask is lightweight and flexible, suitable for small to medium projects. Django is feature-rich, ideal for larger projects. It depends on project size, complexity, and personal preference.',
        5, 5);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (16, 'Flask offers more freedom and customization, making it suitable for specialized projects. Django has a broader perspective with built-in features, making it convenient for rapid development. Consider project requirements and choose accordingly.',
        5, 9);
INSERT INTO comment (id, content, topic_id, user_id)
VALUES (17, 'Flasks perspective lies in simplicity and flexibility, while Django offers a comprehensive framework for efficient web development. The choice depends on project needs, development speed, and the balance between flexibility and convenience.',
        5, 4);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (18, 'Implement automatic logout in Laravel by configuring session lifetime in config/session.php. Use event listeners to track user activity and trigger the logout process after a specified period of inactivity.',
        6, 6);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (19, 'Laravel is a popular PHP framework with great job prospects. It offers a robust ecosystem, extensive documentation, and high developer productivity.',
        7, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (20, 'Symfony is a PHP framework known for stability, scalability, and code reusability. It provides job opportunities for building complex enterprise applications.',
        7, 11);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (21, 'CodeIgniter is a lightweight PHP framework suitable for rapid development. It may have a smaller job market but still offers opportunities.',
        7, 15);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (22, 'In C#, interfaces define a contract for class implementation, promoting loose coupling and abstraction.',
        8, 14);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (23, 'Interfaces enable code reuse, flexibility, and modular design in C#. They''re essential for dependency injection and unit testing.',
        8, 12);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (24, 'Use "interface" keyword to define an interface, and classes implement it using "class : interface" syntax.',
        8, 5);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (25, 'Yes, I''ve worked with C# on MacBook M1. It has been smooth with tools like Visual Studio for Mac and Rider, which have updated for M1 support.',
        9, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (26, 'Yes, Ive worked with C# on MacBook M1. It has been smooth with tools like Visual Studio for Mac and Rider, which have updated for M1 support.',
        9, 7);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (27, 'Recently started C# on MacBook M1. Transition was seamless, and tools like Visual Studio Code and Rider have native M1 support. Improved performance and efficiency noticed.',
        9, 4);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (28, 'React is the most popular JavaScript framework with a robust ecosystem and community support. It''s powerful for complex applications but has a steeper learning curve. On the other hand, Vue is easier to learn and offers flexibility for smaller projects.',
        10, 11);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (29, 'React is popular, powerful for complex apps, but has a learning curve. Vue is beginner-friendly, easy to learn, and flexible.',
        10, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (30, 'Consider project needs, personal preferences. React offers resources, job opportunities. Vue is beginner-friendly, easy to learn',
        10, 6);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (31, 'Haskell is a popular functional programming language known for its strong static typing, pure functions, and powerful type system. It has a vibrant community and offers a unique perspective on functional programming concepts.',
        11, 15);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (32, 'Clojure is a dynamic functional programming language that runs on the Java Virtual Machine (JVM). It emphasizes immutability, simplicity, and interactive development. It has a Lisp-like syntax and a rich set of libraries.',
        11, 13);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (33, 'Scala is a hybrid functional programming language that combines object-oriented and functional programming paradigms. It runs on the JVM and offers powerful features such as type inference, pattern matching, and higher-order functions.',
        11, 10);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (34, 'To delete a row in an SQL table, you can use the DELETE statement. Specify the table name and provide a condition in the WHERE clause that identifies the row(s) to be deleted. For example, the query "DELETE FROM table_name WHERE condition"',
        12, 9);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (35, 'Deleting a row in an SQL table involves using the DELETE statement. Construct the query as "DELETE FROM table_name WHERE condition", where "table_name" is the name of the table and "condition" specifies the criteria for deleting the row(s).',
        12, 12);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (36, 'Docker now has native support for M1 Macs, but occasional compatibility issues with certain images or tools have been reported. Check the Docker documentation and community forums for updates and user experiences.',
        13, 5);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (37, 'Docker supports M1 Macs with native Docker Desktop, but some users have encountered compatibility issues with specific images or dependencies. Ensure compatibility before proceeding.',
        13, 6);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (38, 'Docker offers native support for M1 Macs through Docker Desktop, but image compatibility can vary. Check the Docker community forums and documentation for updates and verify image compatibility.',
        13, 7);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (39, '"The Pragmatic Programmer" is a must-read for IT professionals. It offers practical advice and valuable insights for software development and career advancement.',
        14, 9);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (40, '"Clean Code" by Robert C. Martin (Uncle Bob) is highly recommended. It focuses on writing maintainable code with principles and best practices.',
        14, 12);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (41, '"Sapiens" by Yuval Noah Harari offers a broader perspective on human history, providing context for IT professionals in a societal context.',
        14, 15);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (42, 'I have 6 years of experience as a software engineer, specializing in Java. Currently, I earn around 25,000 PLN per month. It''s important to consider factors like location, company size, and specific skills when discussing earning potential.',
        15, 4);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (43, 'As a freelance web developer with 3 years of experience, my monthly earnings vary depending on projects. On average, I make around 15,000 PLN per month. Freelancing offers flexibility but also requires actively seeking clients.',
        15, 6);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (44, 'I work as a data scientist with 5 years of experience. In my current role, I earn approximately 18,000 PLN per month. Data science skills are in high demand, and salaries can vary based on the industry, company, and specialization within the field.',
        15, 8);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (45, 'Ive been working as a senior UX designer for 8 years, focusing on user research and interaction design. My salary is around 22,000 PLN per month. UX design is a rapidly growing field with increasing demand for skilled professionals.',
        15, 10);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (46, 'To deal with burnout as a software developer, prioritize self-care, set boundaries, seek support from friends and family, and consider therapy if needed. Your mental and emotional well-being is important.',
        16, 12);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (47, 'Coping with burnout involves maintaining work-life balance, engaging in hobbies, managing stress, and seeking guidance from mentors or professionals when necessary. Take care of yourself.',
        16, 5);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (48, 'Address burnout by balancing workload, setting realistic goals, practicing self-care, and seeking support from peers or counselors. Remember to prioritize your well-being as a software developer.',
        16, 14);

INSERT INTO comment (id, content, topic_id, user_id)
VALUES (49, 'Manage burnout by establishing routines, practicing time management, finding joy in activities, and seeking help when needed. Explore new projects and seek support from colleagues. Your well-being matters.',
        16, 11);

--changeset forum-app:insert-like-values
INSERT INTO likes (id, topic_id, user_id) VALUES (1, 1, 10);
INSERT INTO likes (id, topic_id, user_id) VALUES (2, 1, 13);
INSERT INTO likes (id, topic_id, user_id) VALUES (3, 1, 4);
INSERT INTO likes (id, topic_id, user_id) VALUES (4, 2, 7);
INSERT INTO likes (id, topic_id, user_id) VALUES (5, 2, 12);
INSERT INTO likes (id, topic_id, user_id) VALUES (6, 3, 11);
INSERT INTO likes (id, topic_id, user_id) VALUES (7, 4, 5);
INSERT INTO likes (id, topic_id, user_id) VALUES (8, 4, 6);
INSERT INTO likes (id, topic_id, user_id) VALUES (9, 4, 8);
INSERT INTO likes (id, topic_id, user_id) VALUES (10, 6, 9);
INSERT INTO likes (id, topic_id, user_id) VALUES (11, 6, 14);
INSERT INTO likes (id, topic_id, user_id) VALUES (12, 7, 15);
INSERT INTO likes (id, topic_id, user_id) VALUES (13, 8, 4);
INSERT INTO likes (id, topic_id, user_id) VALUES (14, 8, 7);
INSERT INTO likes (id, topic_id, user_id) VALUES (15, 9, 8);
INSERT INTO likes (id, topic_id, user_id) VALUES (16, 10, 7);
INSERT INTO likes (id, topic_id, user_id) VALUES (17, 10, 5);
INSERT INTO likes (id, topic_id, user_id) VALUES (18, 11, 4);
INSERT INTO likes (id, topic_id, user_id) VALUES (19, 12, 9);
INSERT INTO likes (id, topic_id, user_id) VALUES (20, 14, 11);
INSERT INTO likes (id, topic_id, user_id) VALUES (21, 15, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (22, 1, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (23, 1, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (24, 2, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (25, 2, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (26, 2, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (27, 3, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (28, 4, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (29, 4, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (30, 4, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (31, 5, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (32, 5, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (33, 6, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (34, 7, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (35, 8, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (36, 8, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (37, 9, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (38, 9, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (39, 9, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (40, 10, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (41, 10, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (42, 11, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (43, 11, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (44, 12, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (45, 12, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (46, 13, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (47, 13, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (48, 14, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (49, 14, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (50, 15, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (51, 15, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (52, 16, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (53, 16, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (54, 17, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (55, 17, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (56, 18, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (57, 18, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (58, 19, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (59, 19, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (60, 20, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (61, 20, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (62, 21, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (63, 21, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (64, 22, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (65, 22, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (66, 23, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (67, 23, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (68, 24, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (69, 24, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (70, 25, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (71, 25, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (72, 26, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (73, 26, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (74, 27, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (75, 27, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (76, 28, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (77, 28, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (78, 29, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (79, 29, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (80, 30, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (81, 30, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (82, 31, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (83, 31, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (84, 32, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (85, 32, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (86, 33, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (87, 33, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (88, 34, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (89, 34, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (90, 35, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (91, 35, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (92, 36, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (93, 36, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (94, 37, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (95, 37, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (96, 38, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (97, 38, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (98, 39, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (99, 39, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (100, 40, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (101, 40, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (102, 41, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (103, 41, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (104, 42, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (105, 42, 8);
INSERT INTO likes (id, comment_id, user_id) VALUES (106, 43, 9);
INSERT INTO likes (id, comment_id, user_id) VALUES (107, 43, 10);
INSERT INTO likes (id, comment_id, user_id) VALUES (108, 44, 11);
INSERT INTO likes (id, comment_id, user_id) VALUES (109, 44, 12);
INSERT INTO likes (id, comment_id, user_id) VALUES (110, 45, 13);
INSERT INTO likes (id, comment_id, user_id) VALUES (111, 45, 14);
INSERT INTO likes (id, comment_id, user_id) VALUES (112, 46, 15);
INSERT INTO likes (id, comment_id, user_id) VALUES (113, 46, 4);
INSERT INTO likes (id, comment_id, user_id) VALUES (114, 47, 5);
INSERT INTO likes (id, comment_id, user_id) VALUES (115, 47, 6);
INSERT INTO likes (id, comment_id, user_id) VALUES (116, 48, 7);
INSERT INTO likes (id, comment_id, user_id) VALUES (117, 49, 8);

--changeset forum-app:insert-report-values
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (1, 'Not accurate topic', false, 1, 4);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (2, 'Not accurate topic', false, 2, 5);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (3, 'Not accurate topic', false, 3, 6);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (4, 'Not accurate topic', false, 4, 7);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (5, 'Not accurate topic', false, 5, 8);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (6, 'Not accurate topic', false, 6, 9);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (7, 'Not accurate topic', false, 7, 10);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (8, 'Not accurate topic', false, 8, 11);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (9, 'Not accurate topic', false, 9, 12);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (10, 'Not accurate topic', false, 10, 13);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (11, 'Not accurate topic', false, 11, 14);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (12, 'Not accurate topic', false, 12, 15);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (13, 'Not accurate topic', false, 13, 4);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (14, 'Not accurate topic', false, 14, 5);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (15, 'Not accurate topic', false, 15, 6);
INSERT INTO report (id, reason, seen, topic_id, user_id) VALUES (16, 'Not accurate topic', false, 16, 7);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (17, 'Not accurate comment', false, 1, 8);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (18, 'Not accurate comment', false, 2, 5);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (19, 'Not accurate comment', false, 3, 6);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (20, 'Not accurate comment', false, 4, 7);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (21, 'Not accurate comment', false, 5, 8);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (22, 'Not accurate comment', false, 6, 9);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (23, 'Not accurate comment', false, 7, 10);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (24, 'Not accurate comment', false, 8, 11);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (25, 'Not accurate comment', false, 9, 12);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (26, 'Not accurate comment', false, 10, 13);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (27, 'Not accurate comment', false, 11, 14);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (28, 'Not accurate comment', false, 12, 15);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (29, 'Not accurate comment', false, 13, 4);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (30, 'Not accurate comment', false, 14, 5);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (31, 'Not accurate comment', false, 15, 6);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (32, 'Not accurate comment', false, 16, 7);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (33, 'Not accurate comment', false, 17, 8);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (34, 'Not accurate comment', false, 18, 9);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (35, 'Not accurate comment', false, 19, 10);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (36, 'Not accurate comment', false, 20, 11);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (37, 'Not accurate comment', false, 21, 12);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (38, 'Not accurate comment', false, 22, 13);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (39, 'Not accurate comment', false, 23, 14);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (40, 'Not accurate comment', false, 24, 15);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (41, 'Not accurate comment', false, 25, 4);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (42, 'Not accurate comment', false, 26, 5);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (43, 'Not accurate comment', false, 27, 6);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (44, 'Not accurate comment', false, 28, 7);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (45, 'Not accurate comment', false, 29, 8);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (46, 'Not accurate comment', false, 30, 9);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (47, 'Not accurate comment', false, 31, 10);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (48, 'Not accurate comment', false, 32, 11);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (49, 'Not accurate comment', false, 33, 12);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (50, 'Not accurate comment', false, 34, 13);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (51, 'Not accurate comment', false, 35, 14);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (52, 'Not accurate comment', false, 36, 15);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (53, 'Not accurate comment', false, 37, 4);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (54, 'Not accurate comment', false, 38, 5);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (55, 'Not accurate comment', false, 39, 6);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (56, 'Not accurate comment', false, 40, 7);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (57, 'Not accurate comment', false, 41, 8);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (58, 'Not accurate comment', false, 42, 9);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (59, 'Not accurate comment', false, 43, 10);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (60, 'Not accurate comment', false, 44, 11);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (61, 'Not accurate comment', false, 45, 12);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (62, 'Not accurate comment', false, 46, 13);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (63, 'Not accurate comment', false, 47, 14);
INSERT INTO report (id, reason, seen, comment_id, user_id) VALUES (64, 'Not accurate comment', false, 48, 15);

--changeset forum-app:insert-warning-values
INSERT INTO warning (id, user_id) VALUES (1, 4);
INSERT INTO warning (id, user_id) VALUES (2, 5);
INSERT INTO warning (id, user_id) VALUES (3, 6);
INSERT INTO warning (id, user_id) VALUES (4, 7);
INSERT INTO warning (id, user_id) VALUES (5, 8);
INSERT INTO warning (id, user_id) VALUES (6, 9);
INSERT INTO warning (id, user_id) VALUES (7, 10);
INSERT INTO warning (id, user_id) VALUES (8, 11);
INSERT INTO warning (id, user_id) VALUES (9, 12);
INSERT INTO warning (id, user_id) VALUES (10, 13);
INSERT INTO warning (id, user_id) VALUES (11, 14);
INSERT INTO warning (id, user_id) VALUES (12, 15);
INSERT INTO warning (id, user_id) VALUES (13, 4);
INSERT INTO warning (id, user_id) VALUES (14, 4);
INSERT INTO warning (id, user_id) VALUES (15, 5);
INSERT INTO warning (id, user_id) VALUES (16, 6);
INSERT INTO warning (id, user_id) VALUES (17, 7);
INSERT INTO warning (id, user_id) VALUES (18, 8);
INSERT INTO warning (id, user_id) VALUES (19, 9);