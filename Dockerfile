FROM openjdk:alpine

ENV HOME_DIR /usr/src/alexis
ENV JKS_PATH $HOME_DIR/cacert.jks
ENV GOOGLE_APPLICATION_CREDENTIALS $HOME_DIR/google_credentials.json

WORKDIR $HOME_DIR

COPY ./build/libs/alexis.jar $HOME_DIR
COPY ./ $HOME_DIR

RUN \[ -f $GOOGLE_APPLICATION_CREDENTIALS ] || (echo "$GOOGLE_APPLICATION_CREDENTIALS not found."; exit 1) \
  && [ -f $JKS_PATH ] || (echo "$JKS_PATH not found."; exit 1)

RUN ls

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=$JKS_PATH", "-jar", "alexis.jar"]

