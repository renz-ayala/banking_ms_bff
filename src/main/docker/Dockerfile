FROM eclipse-temurin:17-jre-alpine
LABEL authors="renz.ayala"

COPY ./bankingMsBFF-0.0.1-SNAPSHOT.jar ./ms-bff.jar
EXPOSE 11103

ENTRYPOINT ["java", "-Duser.timezone=America/Lima", "-jar", "ms-bff.jar"]

## .\gradlew clean build -x test --no-daemon
## cp .\build\libs\bankingMsBFF-0.0.1-SNAPSHOT.jar  .\src\main\docker\
## cd .\src\main\docker\
## docker container stop ms-bff
## docker build . -t bank/ms-bff:latest
## docker run -d --rm --name ms-bff --env-file .env -p 11103:11103 bank/ms-bff:latest