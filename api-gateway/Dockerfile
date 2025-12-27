FROM bellsoft/liberica-openjdk-alpine:21 AS builder

WORKDIR /build

COPY pom.xml .

RUN apk add --no-cache maven && mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B

FROM bellsoft/liberica-runtime-container:jre-21-musl

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]