FROM amazoncorretto:27
COPY ./target/classes/org /tmp/org
WORKDIR /tmp
ENTRYPOINT ["java", "org.example.Main"]