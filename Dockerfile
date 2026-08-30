FROM gradle:8.5-jdk17 AS build

ARG GITHUB_USER
ARG GITHUB_TOKEN
ENV GITHUB_USER=${GITHUB_USER}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN chmod +x gradlew

RUN ./gradlew clean build -x test --no-daemon

FROM gcr.io/distroless/java17-debian12
COPY --from=build /home/gradle/src/build/libs/*.jar ./ms-bff.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-Duser.timezone=America/Lima","-Dserver.port=11103","-jar","ms-bff.jar"]