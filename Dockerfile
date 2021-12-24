FROM eclipse-temurin:17-jdk-alpine as builder

RUN apk update && apk upgrade
RUN apk add git

RUN mkdir -p /opt/build/
COPY mvnw /opt/build/mvnw
COPY .mvn/ /opt/build/.mvn/
COPY src/main/java /opt/build/src/main/java
COPY src/main/resources/templates /opt/build/src/main/resources/templates
COPY src/main/resources/application-backend.properties /opt/build/src/main/resources/application.properties
COPY pom.xml /opt/build/pom.xml
RUN rm -rf /opt/build/src/main/java/ee/desertgun/jttracker/startup
RUN chmod +x /opt/build/mvnw
WORKDIR /opt/build/
RUN ./mvnw --no-transfer-progress clean package


FROM eclipse-temurin:17-jre-alpine as jre-build
WORKDIR application
COPY --from=builder /opt/build/target/jttracker-1.0.jar application.jar



