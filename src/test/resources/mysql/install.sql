
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
    	SELECT id, first_name firstname, last_name lastname FROM CUSTOMERS;
     	SET code = 0;
  	END
;;

CREATE PROCEDURE list_customers_with_date(OUT db_date DATE, OUT code SMALLINT, OUT msg VARCHAR(50))
   	BEGIN
    	SELECT * FROM CUSTOMERS;
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

CREATE PROCEDURE get_first_table_name_no_resultfields(OUT tableName VARCHAR(50))
  	BEGIN
  		SELECT TABLE_NAME INTO tableName FROM INFORMATION_SCHEMA.TABLES LIMIT 1;
   	END
;;