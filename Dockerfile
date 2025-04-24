# Estágio 1: build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

ARG GITHUB_TOKEN
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

COPY .jenkins/settings.xml /root/.m2/settings.xml

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

RUN rm -f /root/.m2/settings.xml

# Estágio 2: imagem final leve
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
