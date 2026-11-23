INSERT TO user (username, email, password) VALUES
('johnDoe', 'johnDoe@gmail.com','jd38270!')
('janeDoe', 'janeDoe@hotmail.com', 'jd38150!!')
('martinDupont', 'martinDupont@gmail.com', 'md38590!')

INSERT TO user_connections (user_id , friend_id) VALUES
(1,2)
(1,3)
(2,3)

INSERT TO transaction (sender_id, receiver_id, description, amount) VALUES
(1,2,'remboursement',102.00)
(2,3,'anniversaire',52.50)
(1,3,'cinema',12.75)
