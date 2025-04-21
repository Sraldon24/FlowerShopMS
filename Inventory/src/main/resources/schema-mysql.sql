-- ===================================
-- DISABLE FK CHECKS TO AVOID CONFLICTS
-- ===================================
SET FOREIGN_KEY_CHECKS = 0;

-- DROP tables in reverse dependency order
DROP TABLE IF EXISTS flower_options;
DROP TABLE IF EXISTS flowers;
DROP TABLE IF EXISTS inventories;

-- ===================================
-- ENABLE FK CHECKS AGAIN
-- ===================================
SET FOREIGN_KEY_CHECKS = 1;

-- ===================================
-- CREATE INVENTORIES TABLE
-- ===================================
CREATE TABLE inventories (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             inventory_identifier VARCHAR(36) NOT NULL UNIQUE,
                             type VARCHAR(50)
);

-- ===================================
-- CREATE FLOWERS TABLE
-- ===================================
CREATE TABLE flowers (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         flower_identifier VARCHAR(36) NOT NULL UNIQUE,
                         inventory_id VARCHAR(36) NOT NULL,
                         flower_name VARCHAR(100) NOT NULL,
                         flower_color VARCHAR(50),
                         flower_category VARCHAR(50),
                         flower_status VARCHAR(50),
                         stock_quantity INT,
                         price_amount DECIMAL(19,2),
                         price_currency VARCHAR(3),
                         supplier_id VARCHAR(36) NOT NULL,
                         FOREIGN KEY (inventory_id) REFERENCES inventories(inventory_identifier)
);

-- ===================================
-- CREATE FLOWER OPTIONS TABLE
-- ===================================
CREATE TABLE flower_options (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                flower_id INT NOT NULL,
                                option_name VARCHAR(50),
                                option_description VARCHAR(200),
                                option_price DECIMAL(10,2),
                                FOREIGN KEY (flower_id) REFERENCES flowers(id)
);
