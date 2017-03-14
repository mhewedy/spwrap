
CREATE TABLE customers (id INTEGER  PRIMARY KEY,
	first_name VARCHAR2(20),
	last_name VARCHAR2(20))
;;

CREATE SEQUENCE customers_seq START WITH 1
;;

CREATE PROCEDURE create_customer0(firstname IN VARCHAR2, lastname IN VARCHAR2, code OUT NUMBER, msg OUT VARCHAR2) AS
BEGIN
    INSERT INTO customers VALUES (customers_seq.nextval, firstname, lastname);
    code := 0;
END;
;;

CREATE PROCEDURE create_customer(firstname IN VARCHAR2, lastname IN VARCHAR2, custId OUT NUMBER, code OUT NUMBER, msg OUT VARCHAR2) AS
BEGIN
    INSERT INTO customers VALUES (customers_seq.nextval, firstname, lastname);
    custId := customers_seq.currval;
    code := 0;
END;
;;

CREATE PROCEDURE get_customer(custId IN NUMBER, firstname OUT VARCHAR2, lastname OUT VARCHAR2,
		code OUT NUMBER, msg OUT VARCHAR2)
AS
	BEGIN
   		SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = custId;
   		code := 0;
	END;
;;

CREATE PROCEDURE list_customers(rs OUT SYS_REFCURSOR, code OUT NUMBER, msg OUT VARCHAR2)
AS
   	BEGIN
    	open rs for SELECT id, first_name firstname, last_name lastname FROM CUSTOMERS;
     	code := 0;
  	END;
;;

CREATE PROCEDURE list_customers_with_date(db_date OUT DATE, rs OUT SYS_REFCURSOR, code OUT NUMBER, msg OUT VARCHAR2)
AS
   	BEGIN
    	open rs for SELECT * FROM CUSTOMERS;
     	db_date := SYSDATE;
     	code := 0;
  	END;
;;

CREATE PROCEDURE list_tables(rs OUT SYS_REFCURSOR, code OUT NUMBER, msg OUT VARCHAR2)
AS
  	BEGIN
  		open rs for SELECT table_name FROM all_tables;
     	code := 0;
   	END;
;;

CREATE PROCEDURE error_sp(code OUT NUMBER, msg OUT VARCHAR2)
AS
  	BEGIN
     	code := 1;
   	END;
;;


CREATE PROCEDURE first_t_name_no_resultfields(tableName OUT VARCHAR2)
AS
  	BEGIN
  		SELECT table_name INTO tableName FROM all_tables where rownum = 1;
   	END;
;;