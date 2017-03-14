
CREATE TABLE customers (id INT IDENTITY (1, 1) PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20))
;;


/* IN Only*/
CREATE PROCEDURE create_customer0
    @firstname VARCHAR(50),
    @lastname VARCHAR(50),
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
   	BEGIN
    	INSERT INTO customers VALUES (@firstname, @lastname);
    	SET @code = 0;
	END
;;

/* IN */
CREATE PROCEDURE create_customer
    @firstname VARCHAR(50),
    @lastname VARCHAR(50),
    @custId INT OUTPUT,
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
   	BEGIN
    	INSERT INTO customers VALUES (@firstname, @lastname);
    	SET @custId = @@IDENTITY;
    	SET @code = 0;
	END
;;

/* IN, OUT */
CREATE PROCEDURE get_customer
    @custId INT,
    @firstname VARCHAR(50) OUTPUT,
    @lastname VARCHAR(50) OUTPUT,
	@code SMALLINT OUTPUT,
	@msg VARCHAR(50) OUTPUT
AS
	BEGIN
   		SELECT @firstname = first_name, @lastname = last_name FROM customers WHERE id = @custId;
   		SET @code = 0;
	END
;;

/* RS */
CREATE PROCEDURE list_customers
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
   	BEGIN
    	SELECT id, first_name firstname, last_name lastname FROM CUSTOMERS;
     	SET @code = 0;
  	END
;;

/* RS & OUT */
CREATE PROCEDURE list_customers_with_date
    @db_date DATE OUTPUT,
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
   	BEGIN
    	SELECT * FROM CUSTOMERS;
     	SET @db_date = GETDATE();
     	SET @code = 0;
  	END
;;

/* Scalar RS */
CREATE PROCEDURE list_tables
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
	BEGIN
  		SELECT name FROM sys.objects;
     	SET @code = 0;
   	END
;;

/* SP returns error code */
CREATE PROCEDURE error_sp
    @code SMALLINT OUTPUT,
    @msg VARCHAR(50) OUTPUT
AS
	BEGIN
     	SET @code = 1;
   	END
;;

/* Scalar RS */
CREATE PROCEDURE first_t_name_no_resultfields
    @tableName VARCHAR(50) OUTPUT
AS
  	BEGIN
  		SELECT TOP 1 @tableName = name FROM sys.objects;
   	END
;;