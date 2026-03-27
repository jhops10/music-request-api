# Estágio de build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar arquivos de configuração do maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fonte
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Estágio de execução
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar o JAR do estágio de build
COPY --from=builder /app/target/music-request-api-*.jar app.jar

# Expor porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]