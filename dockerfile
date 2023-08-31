FROM openjdk:17

WORKDIR /app

COPY build/libs/educationapp-0.0.1-SNAPSHOT.jar /app/educationapp.jar

EXPOSE 8080

CMD ["java", "-jar", "educationapp.jar"]