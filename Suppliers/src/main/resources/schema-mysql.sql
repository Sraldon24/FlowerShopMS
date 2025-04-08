drop table IF EXISTS suppliers;
create TABLE suppliers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           supplier_identifier VARCHAR(36) NOT NULL UNIQUE,
                           company_name VARCHAR(100) NOT NULL,
                           contact_person VARCHAR(50),
                           email_address VARCHAR(100),
                           username VARCHAR(50),
                           password VARCHAR(255),
                           street_address VARCHAR(100),
                           city VARCHAR(50),
                           province VARCHAR(50),
                           postal_code VARCHAR(20)
);

-- Supplier phone numbers (ElementCollection)

drop table IF EXISTS supplier_phonenumbers;
create TABLE supplier_phonenumbers (
                                       supplier_id INT NOT NULL,
                                       phone_type VARCHAR(20),
                                       phone_number VARCHAR(20),
                                       FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON delete CASCADE
);

-- CREATE EMPLOYEES TABLE
