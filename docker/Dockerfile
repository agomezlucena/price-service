FROM eclipse-temurin:25-jdk AS build

WORKDIR /opt/price-service

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN chmod +x gradlew

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:26-jre

WORKDIR /opt/price-service

COPY --from=build /opt/price-service/build/libs/price-service*.jar price-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "price-service.jar"]