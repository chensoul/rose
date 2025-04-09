FROM eclipse-temurin:8-jre-jammy

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=dev"]

EXPOSE 7010
