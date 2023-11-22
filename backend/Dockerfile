FROM openjdk:17

WORKDIR /app

COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 3030

CMD ["java", "-jar", "app.jar"]