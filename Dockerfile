FROM azul/zulu-openjdk:17-jre-headless-latest
COPY build/libs/*.jar diridi-backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/diridi-backend-0.0.1-SNAPSHOT.jar"]