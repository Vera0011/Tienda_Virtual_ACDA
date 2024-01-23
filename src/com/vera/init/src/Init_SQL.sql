USE virtual_store_acda;

CREATE TABLE IF NOT EXISTS Users_(
	id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    user_ VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password_ VARCHAR(255) NOT NULL,
    creation_date DATETIME NOT NULL,
    modification_date DATETIME DEFAULT NULL,
    balance DECIMAL(65, 2) NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS Products(
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name_ VARCHAR(255) NOT NULL,
    value_ DECIMAL(65, 2) NOT NULL DEFAULT 0.0,
    userID INTEGER
);

CREATE TABLE IF NOT EXISTS Orders(
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
    objectID INTEGER NOT NULL,
    userC INTEGER NOT NULL,
    userV INTEGER NOT NULL,
    date_order DATETIME DEFAULT NULL
);

ALTER TABLE Products ADD CONSTRAINT user_id_ref FOREIGN KEY (userID) REFERENCES Users_(id);

ALTER TABLE Orders ADD CONSTRAINT product_id_ref FOREIGN KEY (objectID) REFERENCES Products(id);
ALTER TABLE Orders ADD CONSTRAINT userC_id_ref FOREIGN KEY (userC) REFERENCES Users_(id);
ALTER TABLE Orders ADD CONSTRAINT userV_id_ref FOREIGN KEY (userV) REFERENCES Users_(id);

DROP TRIGGER IF EXISTS check_duplicate_users;
DROP TRIGGER IF EXISTS add_on_update_now;
DROP TRIGGER IF EXISTS add_date_order;

/* Trigger if user is duplicate + add creation date */
-- @DELIMITER $
CREATE TRIGGER check_duplicate_users BEFORE INSERT ON Users_ FOR EACH ROW
BEGIN
    IF ((SELECT COUNT(*) FROM Users_ U WHERE U.user_ = NEW.user_) != 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Usuario duplicado";
    END IF;

    IF(NEW.creation_date IS NULL) THEN
		SET NEW.creation_date = CURRENT_TIME;
    END IF;
END;$
-- @DELIMITER ;

/* Trigger when user is added */
-- @DELIMITER $
CREATE TRIGGER add_on_update_now BEFORE UPDATE ON Users_ FOR EACH ROW
BEGIN
    SET NEW.modification_date = CURRENT_TIME;
END;$
-- @DELIMITER ;

/* Trigger add creation order */
-- @DELIMITER $
CREATE TRIGGER add_date_order BEFORE INSERT ON Orders FOR EACH ROW
BEGIN
    IF(NEW.date_order IS NULL) THEN
		SET NEW.date_order = CURRENT_TIME;
    END IF;
END;$
-- @DELIMITER ;