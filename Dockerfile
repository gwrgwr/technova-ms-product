# Estágio 1: Build com Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

ARG GITHUB_TOKEN
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

COPY settings.xml /root/.m2/settings.xml

# Copie apenas o POM primeiro (para aproveitar o cache de dependências)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie o restante do código e construa o projeto
COPY src ./src

RUN mvn package -DskipTests

RUN rm -f /root/.m2/settings.xml

# Estágio 2: Imagem final leve
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
