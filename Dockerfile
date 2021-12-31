FROM openjdk:11

RUN mkdir /app

WORKDIR /app

ADD ./target/prekrski-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "prekrski-1.0.0-SNAPSHOT.jar"]