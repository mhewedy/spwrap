
CREATE TABLE customers (id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20))
;;


CREATE PROCEDURE create_customer(IN firstname VARCHAR(50), IN lastname VARCHAR(50), OUT custId INT,
        OUT code SMALLINT, OUT msg VARCHAR(50))
 BEGIN
    INSERT INTO CUSTOMERS VALUES (DEFAULT, firstname, lastname);
    SET custId = LAST_INSERT_ID();
    SET code = 0;
 END
;;