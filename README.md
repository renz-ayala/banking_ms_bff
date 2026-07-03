# Arquitectura de Microservicios y BFF

Este repositorio contiene la arquitectura base de servicios financieros reactivos.

Desarrollado con :
* **Java 17**
* **Spring Boot 3.5.13**
* **Spring WebFlux**

---

## Estructura del Proyecto
* **`banking-starter-crypt`**: Starter que provee el componente de criptografía (debe instalarse de forma prioritaria en el repositorio local).
* **`bank_ms_clients`**: Microservicio encargado de la gestión de información de clientes.
* **`bank_ms_products`**: Microservicio encargado de la gestión de productos financieros.
* **`bank_ms_bff`**: Microservicio BFF encargada de la orquestación reactiva.
* **`docker-compose.yml`**: Compositor para docker

## Detalle importante:
En cada proyecto debe reemplazar el archivo `.env.template` por `.env` y colocar las variables correspondientes,
en caso desee usar un IDE, debe configurar las variables de entorno guiandose con el mismo archivo.

Este proyecto no incluye la base de datos. Por lo que tendrá que diseñar sus propios datos.
Puede usar como guía las siguientes sentencias SQL:
```bash
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

CREATE TABLE user1.credentials(
    credential_id SERIAL primary key,
    username varchar(50) unique not null,
    password varchar(50) not null
);

INSERT INTO CLIENTS (unique_id, names, last_name, document_type, document_num)
VALUES ('LMA-8822', 'Rodolfo', 'Reno', 'DNI', '44556677');

INSERT INTO PRODUCTS (unique_client_id, product_type, product_name, balance)
VALUES 
('CLI-8822', 'CUENTA_AHORRO', 'Ahorro Soles Pueblos Libres', 2500.80),
('CLI-8822', 'TARJETA_CREDITO', 'Mastercard Black', 10500.00);

insert into user1.credentials (username, password)
values ('bankingmsbff', '2444666668888888')
```

---

## Requisitos Previos
* Java SE Development Kit (JDK) 17.
* Docker Desktop instalado y en ejecución.

---

## Guía de Despliegue Local
Antes de iniciar; las carpetas de los microservicios deben estar en una misma carpeta, y junto a ellos se debe copiar el archivo `docker-compose.yml`.
Esta esta ubicada en el proyecto bff: src/main/docker


Ejecute los siguientes comandos para compilar e iniciar el entorno:

### 1. Instalación del Starter de Criptografía
Debido a que los microservicios dependen del Starter local, instálelo primero en el repositorio local de Gradle:
Ingrese en la carpeta del proyecto y ejecute
```bash
./gradlew publishToMavenLocal
```

### 2. Compilación y Construcción de Artefactos (JARs)
Genere los empaquetados ejecutables de cada módulo omitiendo las pruebas unitarias:
Ingrese en cada carpeta de los microservicios y ejecutar en cada uno los siguiente comandos.
En el archivo dockerfile verá una guia mas específica si desea desplegarlos de forma individual.

Ms-Bff
```bash
.\gradlew clean build -x test --no-daemon
cp .\build\libs\bankingMsBFF-0.0.1-SNAPSHOT.jar  .\src\main\docker\
```

Ms-Clients
```bash
.\gradlew clean build -x test --no-daemon
cp .\build\libs\bankingMsClients-0.0.1-SNAPSHOT.jar  .\src\main\docker\
```

Ms-Products
```bash
.\gradlew clean build -x test --no-daemon
cp .\build\libs\bankingMsProducts-0.0.1-SNAPSHOT.jar  .\src\main\docker\
```

### 3. Orquestación del Entorno con Docker Compose
Una vez generados los archivos .jar, ejecute el despliegue centralizado de los contenedores:

```bash
docker compose up --build -d
```
