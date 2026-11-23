CREATE DATABASE pay_my_buddy; 
USE pay_my_buddy; 

CREATE TABLE user ( 
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
username VARCHAR(100), 
email VARCHAR(100) NOT NULL UNIQUE, 
password VARCHAR(100) NOT NULL
); 

CREATE TABLE user_connections ( 
user_id INT NOT NULL, 
friend_id INT NOT NULL, 
PRIMARY KEY (user_id, friend_id), 
FOREIGN KEY (user_id) REFERENCES user(id), 
FOREIGN KEY (friend_id) REFERENCES user(id) 
); 

CREATE TABLE transaction ( 
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
sender_id INT NOT NULL, 
receiver_id INT NOT NULL, 
description VARCHAR(200), 
amount DECIMAL(10,2), 
FOREIGN KEY (sender_id) REFERENCES user(id), 
FOREIGN KEY (receiver_id) REFERENCES user(id) 
); 
