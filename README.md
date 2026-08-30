# Arquitectura de Microservicios y BFF

Arquitectura base de servicios financieros reactivos basada en microservicios asíncronos y patrón BFF.

---

## Especificaciones Técnicas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.5.13
* **Programación Reactiva:** Spring WebFlux
* **Gestor de dependencias:** Gradle

---

## Estructura del Proyecto

* **`greckrypto`**: Starter autoconfigurable que provee el componente de criptografía (debe instalarse de forma prioritaria en el repositorio local).
* **`bank_ms_clients`**: Microservicio encargado de la gestión de información de clientes.
* **`bank_ms_products`**: Microservicio encargado de la gestión de productos financieros.
* **`bank_ms_bff`**: Microservicio BFF encargado de la orquestación reactiva.
* **`docker-compose.yml`**: Orquestador de contenedores para el entorno local.
---

## Interfaz de Usuario (Cliente Demo)

Para probar la integración y el flujo completo de los microservicios sin necesidad de utilizar herramientas como Postman, se desarrolló una interfaz web minimalista:

* **Repositorio UI:** [bank_ui](https://github.com/renz-ayala/bank_ui)

Dentro de dicho repositorio se encuentra la documentación e instrucciones para desplegar la interfaz y ejecutar la demostración visual.

---

## Configuración del Entorno

En cada microservicio debe crear el archivo `.env` tomando como referencia el archivo `.env.template` e ingresar las variables de entorno correspondientes.

Asegúrese de incluir la clave de cifrado requerida por el starter criptográfico en la configuración del archivo de propiedades (`application.yml` / `application.properties`):
```bash
secret_keys:
  codes:
    crypt_key: ${CRYPT_KEY}
```

En caso de ejecutar el proyecto desde un IDE, configure estas mismas variables de entorno dentro del perfil de ejecución correspondiente.

---

## Esquema e Inserción de Datos (SQL)

Este repositorio no incluye la instancia de base de datos provisionada. Para inicializar el modelo relacional, ejecute las siguientes sentencias DDL y DML en su motor PostgreSQL:
```bash
CREATE TABLE CLIENTS (
    client_id SERIAL PRIMARY KEY,
    unique_id VARCHAR(50) UNIQUE NOT NULL,
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

CREATE TABLE user1.credentials (
    credential_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
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

INSERT INTO user1.credentials (username, password)
VALUES ('ggrenz', 'qwerty1234');
```
---

## Requisitos Previos

* Java SE Development Kit (JDK) 17.
* Docker Desktop instalado y en ejecución.

---

## Guía de Despliegue Local

Las carpetas de los microservicios deben residir en el mismo nivel de directorio junto con el archivo `docker-compose.yml` (ubicado originalmente en `bank_ms_bff/src/main/docker`).

### 1. Resolución de Dependencia del Starter Criptográfico

Para la resolución de la librería `greckrypto`, elija una de las dos modalidades según su flujo de trabajo:

#### Opción A: Compilación e Instalación Local (Recomendado para Desarrollo Local)
Si dispone del código fuente del starter criptográfico en su entorno local, publíquelo primero en el repositorio Maven local antes de compilar los microservicios:
```bash
cd greckrypto
./gradlew publishToMavenLocal
```

Asegúrese de que el archivo `build.gradle` de cada microservicio contenga el repositorio local:
```bash
repositories {
    mavenLocal()
    mavenCentral()
}
```

#### Opción B: Descarga desde GitHub Packages (Registro Remoto)
Si prefiere consumir el paquete directamente desde el repositorio remoto de GitHub Packages, configure las credenciales en su entorno terminal mediante un Token de Acceso Personal (PAT) con permisos `read:packages`:
```bash
export GITHUB_USER="tu-usuario-github"
export GITHUB_TOKEN="tu-github-personal-access-token"
```
En la configuración de construcción (`build.gradle`), defina el repositorio remoto:
```bash
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/renz-ayala/greckrypto_starter")
        credentials {
            username = System.getenv("GITHUB_USER")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
```
### 2. Compilación y Construcción de Artefactos (JARs)
Genere los binarios ejecutables de cada módulo omitiendo las pruebas unitarias:

#### Ms-Bff
```bash
cd bank_ms_bff
./gradlew clean build -x test --no-daemon
cp ./build/libs/bankingMsBFF-0.0.1-SNAPSHOT.jar ./src/main/docker/
```

#### Ms-Clients
```bash
cd bank_ms_clients
./gradlew clean build -x test --no-daemon
cp ./build/libs/bankingMsClients-0.0.1-SNAPSHOT.jar ./src/main/docker/
```
#### Ms-Products
```bash
cd bank_ms_products
./gradlew clean build -x test --no-daemon
cp ./build/libs/bankingMsProducts-0.0.1-SNAPSHOT.jar ./src/main/docker/
```
---

### 3. Orquestación del Entorno con Docker Compose
Una vez generados y ubicados los artefactos `.jar`, inicie la construcción y ejecución de los contenedores:
```bash
docker compose up --build -d
```
