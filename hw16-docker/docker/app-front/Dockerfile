FROM bellsoft/liberica-openjdk-alpine-musl:17.0.9
COPY ./front/target/front-app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
