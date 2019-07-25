FROM openjdk:11.0.4-jre-slim
RUN adduser -u 1001 -Sh /home/alexis alexis
WORKDIR /home/alexis/
ENTRYPOINT ["java", "-jar", "alexis.jar"]
