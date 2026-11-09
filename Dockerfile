# ============================================================
# Etapa 1 - BUILD: compila o projeto com Maven e gera target/app.jar
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /opt/app

# Copia primeiro só o pom.xml para aproveitar o cache das dependências
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copia o código-fonte e empacota (testes são pulados: precisam de banco)
COPY src ./src
RUN mvn -B clean package -DskipTests

# ============================================================
# Etapa 2 - RUNTIME: imagem final leve, só com o JRE e o .jar
# ============================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app

# Executa com usuário sem privilégios de root
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /opt/app/target/app.jar app.jar
USER spring

EXPOSE 8080

# DB_URL, DB_USER e DB_PASSWORD são passados em tempo de execução
# (docker run -e ... / variáveis de ambiente do Azure). Nada de senha na imagem.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
