FROM openjdk:11.0.3-jre-slim
RUN adduser -u 1001 -Sh /home/alexis alexis

ENV HOME_DIR /usr/src/alexis
ENV JKS_PATH $HOME_DIR/cacert.jks
ENV GOOGLE_APPLICATION_CREDENTIALS $HOME_DIR/google_credentials.json
WORKDIR $HOME_DIR

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=$JKS_PATH", "-jar", "alexis.jar"]

