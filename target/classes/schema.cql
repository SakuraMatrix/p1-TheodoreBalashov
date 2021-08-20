CREATE KEYSPACE IF NOT EXISTS paintingSeller WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE paintingSeller;

CREATE TABLE IF NOT EXISTS user (
    user_id int,
    username text,
    password text,
    balance double,
    PRIMARY KEY (user_id, username)
);

CREATE TABLE IF NOT EXISTS paintingAction (
    action_id int,
    user_id int,
    painting_id int,
    action text,
    amount double,
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

INSERT INTO paintingSeller.painting (painting_id, owner, title, url, description, author, isForSale, price) values (1, 101, 'forestsOfPaimpont', 'paintingURL', 'surrealist', 'magritte', false, 0);
INSERT INTO paintingSeller.painting (painting_id, owner, title, url, description, author, isForSale, price) values (2, 101, 'birthOfVenus', 'paintingURL', 'renaissance', 'botticelli', false, 0);
INSERT INTO paintingSeller.painting (painting_id, owner, title, url, description, author, isForSale, price) values (3, 103, 'kitty', 'paintingURL', 'crayonDrawing', 'mrboots', false, 0);

INSERT INTO paintingSeller.user (user_id, username, password, balance) values (101, 'rael', 'iamrael', 500);
INSERT INTO paintingSeller.user (user_id, username, password, balance) values (102, 'peterGabriel', 'shockthemonkey', 200);
INSERT INTO paintingSeller.user (user_id, username, password, balance) values (103, 'mrboots', 'password123', 0);