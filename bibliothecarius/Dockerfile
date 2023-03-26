FROM coderabbit214/jdk:17.0.6
LABEL maintainer=bibliothecarius

COPY . /code

WORKDIR /code

RUN ./gradlew bootJar

COPY build/libs/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -jar /app.jar"]