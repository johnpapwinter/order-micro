FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline && \
    rm -rf /root/.m2/repository

COPY src ./src
RUN ./mvnw clean package -DskipTests && \
    mv target/*.jar app.jar && \
    rm -rf target/ && \
    rm -rf ~/.m2/

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

