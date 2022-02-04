create table book
(
    ID          serial,
    TITLE       varchar(255) not null,
    AUTHOR_ID   integer
);




create table book_request
(
    ID          serial,
    TITLE       varchar(255) not null,
    FIRST_NAME   varchar(255) not null,
    Email varchar(255)
);