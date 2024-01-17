FROM bellsoft/liberica-openjdk-alpine-musl:17.0.9
COPY ./library/target/library-app.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
