CREATE TABLE CLIENTS (
                         client_id SERIAL PRIMARY KEY,
                         unique_id VARCHAR(50) UNIQUE NOT NULL, -- BFF unique key / id
                         names VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         document_type VARCHAR(20) NOT NULL,
                         document_num VARCHAR(20) NOT NULL
);

CREATE TABLE PRODUCTS (
                          product_id SERIAL PRIMARY KEY,
                          unique_client_id VARCHAR(50) NOT NULL,
                          product_type VARCHAR(50) NOT NULL,
                          product_name VARCHAR(100) NOT NULL,
                          balance DECIMAL(15, 2) NOT NULL
);

INSERT INTO CLIENTS (unique_id, names, last_name, document_type, document_num) VALUES
                                                                                   ('001948201', 'Renz', 'Ayala', 'DNI', '44556677'),
                                                                                   ('002104852', 'Bruno', 'Díaz', 'DNI', '71829304'),
                                                                                   ('000849201', 'Melania', 'Urbina', 'DNI', '10293847'),
                                                                                   ('003492018', 'Naomi', 'Ayala', 'DNI', '48392019'),
                                                                                   ('000049182', 'INVERSIONES SANTISIMA TRINIDAD S.A.C.', 'S.A.C.', 'RUC', '20601234567'),
                                                                                   ('001849203', 'Charles', 'Darwin', 'CE', '001293847');

INSERT INTO PRODUCTS (unique_client_id, product_type, product_name, balance) VALUES
                                                                                 ('001948201', 'CUENTA_AHORRO', 'Ahorro Soles Pueblos Libres', 2500.80),
                                                                                 ('001948201', 'TARJETA_CREDITO', 'Mastercard Black', 10500.00),
                                                                                 ('002104852', 'CUENTA_AHORRO', 'Cuenta Simple Soles', 1240.50),
                                                                                 ('002104852', 'CUENTA_AHORRO', 'Cuenta Millonaria Dólares', 450.00),
                                                                                 ('002104852', 'TARJETA_CREDITO', 'Visa Platinum', 3200.00),
                                                                                 ('000849201', 'CUENTA_SUELDO', 'Cuenta Sueldo Soles', 5800.00),
                                                                                 ('000849201', 'PRESTAMO_PERSONAL', 'Crédito Efectivo Empresarial', -12500.00),
                                                                                 ('003492018', 'CUENTA_AHORRO', 'Cuenta Simple Soles', 85.20),
                                                                                 ('000049182', 'CUENTA_CORRIENTE', 'Cuenta Corriente Pyme Soles', 85400.00),
                                                                                 ('000049182', 'CUENTA_CORRIENTE', 'Cuenta Corriente Dólares', 15200.50),
                                                                                 ('000049182', 'TARJETA_CREDITO', 'Visa Business Gold', 25000.00),
                                                                                 ('001849203', 'CUENTA_AHORRO', 'Cuenta Sueldo Soles', 3100.00),
                                                                                 ('001849203', 'TARJETA_CREDITO', 'Amex Platinum', 8000.00);

select * from public.clients
select * from public.products

CREATE TABLE user1.credentials(
                                  credential_id SERIAL primary key,
                                  username varchar(50) unique not null,
                                  password varchar(50) not null
);

insert into user1.credentials (username, password)
values ('ggrenz', 'qwerty1234')

select * from user1.credentials