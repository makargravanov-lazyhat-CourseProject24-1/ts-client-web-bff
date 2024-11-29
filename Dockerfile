FROM eclipse-temurin:21

WORKDIR /app

ARG name

ENV JAR_NAME "${name}.jar"

RUN groupadd spring && useradd -g spring spring
USER spring:spring

COPY ./build/libs/${JAR_NAME} ${JAR_NAME}

ENTRYPOINT ["ls", "/app"]

ENTRYPOINT ["java", "-jar", "/app/ts-client-web-bff.jar"]