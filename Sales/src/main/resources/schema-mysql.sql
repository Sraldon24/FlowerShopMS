drop table IF EXISTS sales;
create TABLE sales (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       purchase_id VARCHAR(36) NOT NULL UNIQUE,
                       sale_status VARCHAR(50),
                       number_of_monthly_payments INT,
                       monthly_payment_amount DECIMAL(19,2),
                       down_payment_amount DECIMAL(19,2),
                       sale_offer_date DATE,
                       amount DECIMAL(19,2),
                       currency VARCHAR(3),
                       supplier_id VARCHAR(36),
                       employee_id VARCHAR(36),
                       inventory_id VARCHAR(36),
                       flower_id VARCHAR(36)

                       -- Foreign keys removed to avoid cross-service FK issues
--                       ,FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_identifier),
--                       FOREIGN KEY (employee_id) REFERENCES employees(employee_identifier),
--                       FOREIGN KEY (inventory_id) REFERENCES inventories(inventory_identifier),
--                       FOREIGN KEY (flower_id) REFERENCES flowers(flower_identifier)
);
