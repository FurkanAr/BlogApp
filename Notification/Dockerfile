FROM openjdk:19
EXPOSE 8082
ADD target/dockernotification.jar dockernotification.jar
ENTRYPOINT ["java", "-jar", "/dockernotification.jar"]