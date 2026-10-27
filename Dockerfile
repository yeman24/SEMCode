FROM amazoncorretto:17
COPY target/SEMCode-0.1.0.3.jar /app.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "semApp.jar"]