CREATE TABLE users (
    id bigserial primary key,
    username varchar not null,
    password varchar not null,
    authority varchar not null
);

INSERT INTO users (username,password,authority)
VALUES  ('Biba','$2a$10$ibgqgPfk9I.PEU9Mu6GqQemug1cRNGEb1hzmVOpOTnE6UG7cj5DLK','ROLE_USER'),
        ('Boba','$2a$10$uWxxVZK0L9TAExnc58gZSODL6cY1VO/5cD3gVijfZjlosJ7o9C3cS','ROLE_USER'),
        ('Mefistofele','$2a$10$ohmZ/S3B2dlFebtTA4XxJu13gdnxoBfgCe/1sb9cBkcIHUsYvEQU6','ROLE_USER');