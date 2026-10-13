CREATE TABLE tab_employee (
    employee_id INT IDENTITY(1,1) NOT NULL,
    name VARCHAR(30) NOT NULL,
    address VARCHAR(60) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(40) NOT NULL,
    department VARCHAR(20) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    admission_date DATETIMEOFFSET NOT NULL,
    CONSTRAINT PK_tab_employee
    PRIMARY KEY (employee_id)
);