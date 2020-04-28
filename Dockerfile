# Smallest appropriate image.
FROM openjdk:11.0.7-jre-slim

LABEL maintainer="seth@elypia.org"

# Create a user so we don't run as root.
RUN useradd -mu 1001 -s /bin/bash alexis
USER alexis
WORKDIR /home/alexis/

# Copy over the application, it's just a single jar file.
COPY ./build/libs/alexis-*.jar /home/elypia/alexis.jar

# On startup, execute the jar.
ENTRYPOINT ["java", "-jar", "alexis.jar"]

# Check the README.md to find our what else you'll want to do with the resulting image.
