CREATE KEYSPACE IF NOT EXISTS paintingSeller WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE paintingSeller;

CREATE TABLE IF NOT EXISTS user (
    user_id int,
    username text,
    password text,
    PRIMARY KEY (user_id, username)
);

CREATE TABLE IF NOT EXISTS paintingAction (
    action_id int,
    user_id int,
    painting_id int,
    action text,
    amount double;
    PRIMARY KEY (action_id)
);

CREATE TABLE IF NOT EXISTS userAction (
    action_id int,
    user_id int,
    action text,
    amount double,
    PRIMARY KEY (action_id)
);

CREATE TABLE IF NOT EXISTS painting (
    painting_id int,
    owner int,
    title text,
    url text,
    description text,
    author text,
    isForSale boolean,
    price double,
    PRIMARY KEY (painting_id)
);

INSERT INTO paintingSeller.painting (painting_id, owner, title, url, description, author, isForSale, price) values (100, 200, 'example1', 'url1', 'example1', 'admin1', false, 1.0);
INSERT INTO paintingSeller.painting (painting_id, owner, title, url, description, author, isForSale, price) values (101, 201, 'example2', 'url2', 'example2', 'admin2', false, 2.0);