USE mydb;

-- SHOW VARIABLES LIKE "secure_file_priv";


-- -----------------------------------------------------
-- Load Table `mydb`.`Gestor`
-- -----------------------------------------------------
LOAD DATA INFILE 'csv/gestor.csv'
    INTO TABLE Gestor
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS;


-- -----------------------------------------------------
-- Load Table `mydb`.`Palete`
-- -----------------------------------------------------
LOAD DATA INFILE 'csv/palete.csv'
    INTO TABLE Palete
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS;


-- -----------------------------------------------------
-- Load Table `mydb`.`Entrega`
-- -----------------------------------------------------
LOAD DATA INFILE 'csv/entrega.csv'
    INTO TABLE Entrega
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS;

-- -----------------------------------------------------
-- Load Table `mydb`.`Requisicao`
-- -----------------------------------------------------
LOAD DATA INFILE 'csv/requisicao.csv'
    INTO TABLE Requisicao
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS;



-- -----------------------------------------------------
-- Load Table `mydb`.`Robots`
-- -----------------------------------------------------
LOAD DATA INFILE 'csv/robots.csv'
    INTO TABLE Robots
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS;
