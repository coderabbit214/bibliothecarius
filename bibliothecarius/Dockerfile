FROM coderabbit214/jdk:17.0.6
LABEL maintainer=bibliothecarius

COPY . /code

WORKDIR /code

RUN ./gradlew bootJar && mv ./build/libs/bibliothecarius-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -jar /app.jar"]