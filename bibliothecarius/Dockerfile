FROM openjdk:17-jdk
LABEL maintainer=bibliothecarius

COPY . /code

WORKDIR /code

RUN microdnf install findutils

RUN ./gradlew bootJar
RUN mv ./build/libs/bibliothecarius-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -jar /app.jar"]
