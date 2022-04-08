CREATE TABLE users (
    id bigserial primary key,
    username varchar not null,
    password varchar not null,
    authority varchar not null
);

CREATE TABLE categories (
    id bigserial primary key,
    programming boolean not null default false,
    misc boolean not null default false,
    dark boolean not null default false,
    pun boolean not null default false,
    spooky boolean not null default false,
    christmas boolean not null default false
);

CREATE TABLE flags (
    id bigserial primary key,
    nsfw boolean not null default false,
    religious boolean not null default false,
    political boolean not null default false,
    racist boolean not null default false,
    sexist boolean not null default false,
    explicit boolean not null default false
);

CREATE FUNCTION api_preferences() RETURNS trigger AS $api_preferences$
    BEGIN
        INSERT INTO categories DEFAULT VALUES;
        INSERT INTO flags DEFAULT VALUES;
        RETURN NEW;
    END;
$api_preferences$ LANGUAGE plpgsql;

CREATE TRIGGER api_preferences AFTER INSERT ON users
FOR EACH ROW EXECUTE PROCEDURE api_preferences();

INSERT INTO users (username,password,authority)
VALUES  ('Biba','$2a$10$ibgqgPfk9I.PEU9Mu6GqQemug1cRNGEb1hzmVOpOTnE6UG7cj5DLK','ROLE_USER'),
        ('Boba','$2a$10$uWxxVZK0L9TAExnc58gZSODL6cY1VO/5cD3gVijfZjlosJ7o9C3cS','ROLE_USER'),
        ('Mefistofele','$2a$10$ohmZ/S3B2dlFebtTA4XxJu13gdnxoBfgCe/1sb9cBkcIHUsYvEQU6','ROLE_USER');