drop table IF EXISTS employees;

-- CREATE SUPPLIERS TABLE
create TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           employee_identifier VARCHAR(36) NOT NULL UNIQUE,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100),
                           job_title VARCHAR(50)
);

-- CREATE INVENTORIES TABLE
