-- DROP TABLE IF EXISTS to clean up before creating
DROP TABLE IF EXISTS employees;

-- CREATE EMPLOYEES TABLE with PostgreSQL syntax
CREATE TABLE employees (
                           id SERIAL PRIMARY KEY,
                           employee_identifier VARCHAR(36) NOT NULL UNIQUE,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100),
                           job_title VARCHAR(50)
);
