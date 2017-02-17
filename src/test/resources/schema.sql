
CREATE TABLE customers (id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  
	first_name VARCHAR(20), 
	last_name VARCHAR(20))
;;

/* IN */
CREATE PROCEDURE create_customer(firstname VARCHAR(50), lastname VARCHAR(50), OUT custId INT, 
		OUT code SMALLINT, OUT msg VARCHAR(50))
   	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	INSERT INTO CUSTOMERS VALUES (DEFAULT, firstname, lastname);
    	SET custId = IDENTITY();
    	SET code = 0;
	END
;;

/* IN, OUT */
CREATE PROCEDURE get_customer(IN custId INT, OUT firstname VARCHAR(50), OUT lastname VARCHAR(50), 
		OUT code SMALLINT, OUT msg VARCHAR(50)) 
	READS SQL DATA
	BEGIN ATOMIC
   		SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = custId;
   		SET code = 0;
	END
;;

/* RS */
CREATE PROCEDURE list_customers(OUT code SMALLINT, OUT msg VARCHAR(50))
   	READS SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	DECLARE result CURSOR FOR SELECT * FROM CUSTOMERS;
     	OPEN result;
     	SET code = 0;
  	END
;;

/* RS & OUT */
CREATE PROCEDURE list_customers_with_date(OUT db_date DATE, OUT code SMALLINT, OUT msg VARCHAR(50))
   	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	DECLARE result CURSOR FOR SELECT * FROM CUSTOMERS;
     	OPEN result;
     	SET db_date = CURRENT_DATE;
     	SET code = 0;
  	END
;;

/* Scalar RS */
CREATE PROCEDURE list_tables(OUT code SMALLINT, OUT msg VARCHAR(50))
	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
  	BEGIN ATOMIC
  		DECLARE result CURSOR FOR SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES;
     	OPEN result;
     	SET code = 0;
   	END
;;

/* SP returns error code */
CREATE PROCEDURE error_sp(OUT code SMALLINT, OUT msg VARCHAR(50))
	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
  	BEGIN ATOMIC
     	SET code = 1;
   	END
;;


/* Scalar RS */
CREATE PROCEDURE get_first_table_name_no_resultfields(OUT tableName VARCHAR(50))
	READS SQL DATA
  	BEGIN ATOMIC
  		SELECT LIMIT 0 1 TABLE_NAME INTO tableName FROM INFORMATION_SCHEMA.SYSTEM_TABLES;
   	END
;;