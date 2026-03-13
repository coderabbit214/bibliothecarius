FROM eclipse-temurin:17-jdk-jammy AS builder
LABEL maintainer=bibliothecarius

WORKDIR /workspace
COPY . .

RUN chmod +x ./gradlew \
	&& ./gradlew --no-daemon bootJar

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
