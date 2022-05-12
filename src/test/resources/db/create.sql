CREATE DATABASE jadle_test;
\c jadle_test;

CREATE TABLE restaurants (
 id int PRIMARY KEY auto_increment,
 name VARCHAR,
 address VARCHAR,
 zipcode VARCHAR,
 phone VARCHAR,
 website VARCHAR,
 email VARCHAR
);

CREATE TABLE foodtypes (
 id int PRIMARY KEY auto_increment,
 name VARCHAR
);


CREATE TABLE IF NOT EXISTS reviews (
 id int PRIMARY KEY auto_increment,
 writtenby VARCHAR,
 content VARCHAR,
 rating VARCHAR,
 restaurantid INTEGER,
 createdat BIGINT
);

CREATE TABLE IF NOT EXISTS restaurants_foodtypes (
 id int PRIMARY KEY auto_increment,
 foodtypeid INTEGER,
 restaurantid INTEGER
);