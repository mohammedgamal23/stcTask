FROM openjdk:17

WORKDIR /app

COPY target/spring-app.jar /app/spring-app.jar

EXPOSE 8080

CMD ["java", "-jar", "spring-app.jar"]

