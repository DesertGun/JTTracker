FROM adoptopenjdk:11-jdk-hotspot as builder

RUN mkdir -p /opt/build/
COPY mvnw /opt/build/mvnw
COPY .mvn/ /opt/build/.mvn/
COPY src/main/java /opt/build/src/main/java
COPY src/main/resources/application-backend.properties /opt/build/src/main/resources/application.properties
COPY pom.xml /opt/build/pom.xml
RUN chmod +x /opt/build/mvnw
WORKDIR /opt/build/
RUN ./mvnw --no-transfer-progress clean package


FROM adoptopenjdk:11-jre-hotspot as layer
WORKDIR application
COPY --from=builder /opt/build/target/jttracker-0.9.jar application.jar



