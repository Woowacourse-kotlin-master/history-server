FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

COPY env/prod.env /env/prod.env
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
