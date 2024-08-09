show databases;
use I_Pos;


CREATE TABLE Customer (
                         id VARCHAR(255) PRIMARY KEY,
                         name VARCHAR(255) ,
                         address VARCHAR(255),
                         salary INT
);


CREATE TABLE Item(
  itemId VARCHAR(255) PRIMARY KEY ,
  itemName VARCHAR(255),
  itemQty INT,
  itemPrice INT

);