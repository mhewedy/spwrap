
CREATE TABLE customers (id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20))
;;


CREATE PROCEDURE create_customer0(IN firstname VARCHAR(50), IN lastname VARCHAR(50),
        OUT code SMALLINT, OUT msg VARCHAR(50))
 BEGIN
    INSERT INTO customers VALUES (DEFAULT, firstname, lastname);
    SET code = 0;
 END
;;

CREATE PROCEDURE create_customer(IN firstname VARCHAR(50), IN lastname VARCHAR(50), OUT custId INT,
        OUT code SMALLINT, OUT msg VARCHAR(50))
 BEGIN
    INSERT INTO customers VALUES (DEFAULT, firstname, lastname);
    SET custId = LAST_INSERT_ID();
    SET code = 0;
 END
;;

CREATE PROCEDURE get_customer(IN custId INT, OUT firstname VARCHAR(50), OUT lastname VARCHAR(50),
		OUT code SMALLINT, OUT msg VARCHAR(50))
	BEGIN
   		SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = custId;
   		SET code = 0;
	END
;;

CREATE PROCEDURE list_customers(OUT code SMALLINT, OUT msg VARCHAR(50))
   	BEGIN
    	SELECT id, first_name firstname, last_name lastname FROM customers;
     	SET code = 0;
  	END
;;

CREATE PROCEDURE list_customers_with_date(OUT db_date DATE, OUT code SMALLINT, OUT msg VARCHAR(50))
   	BEGIN
    	SELECT * FROM customers;
     	SET db_date = CURRENT_DATE;
     	SET code = 0;
  	END
;;

CREATE PROCEDURE list_tables(OUT code SMALLINT, OUT msg VARCHAR(50))
  	BEGIN
  		SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;
     	SET code = 0;
   	END
;;

CREATE PROCEDURE error_sp(OUT code SMALLINT, OUT msg VARCHAR(50))
  	BEGIN
     	SET code = 1;
   	END
;;

CREATE PROCEDURE first_t_name_no_resultfields(OUT tableName VARCHAR(50))
  	BEGIN
  		SELECT TABLE_NAME INTO tableName FROM INFORMATION_SCHEMA.TABLES LIMIT 1;
   	END
;;

CREATE TABLE component (
    id INT AUTO_INCREMENT PRIMARY KEY,
    field0 VARCHAR(50), field1 VARCHAR(50), field2 VARCHAR(50), field3 VARCHAR(50), field4 VARCHAR(50),
    field5 VARCHAR(50), field6 VARCHAR(50), field7 VARCHAR(50), field8 VARCHAR(50), field9 VARCHAR(50))
;;

CREATE PROCEDURE fill_component()
    BEGIN
         DECLARE i int DEFAULT 1;
            WHILE i <= 500 DO
                INSERT INTO component VALUES (DEFAULT, MD5(i), MD5(i), MD5(i), MD5(i), MD5(i), MD5(i), MD5(i), MD5(i), MD5(i), MD5(i));
                SET i = i + 1;
            END WHILE;
    END
;;

CREATE PROCEDURE list_components()
    BEGIN
        SELECT field0, field1, field2, field3, field4, field5, field6, field7, field8, field9 FROM component;
    END
;;