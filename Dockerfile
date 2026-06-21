# Use the official gradle image to create a build artifact.
# https://hub.docker.com/_/gradle
FROM gradle:8.5 AS builder
LABEL stage=builder

# 1. Only dependency-related files first
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# 2. Download dependencies (this layer will be cached)
RUN gradle dependencies --no-daemon

# 3. Now copy source code (changes here won't affect deps layer)
COPY src ./src

# 4. Build
RUN gradle build --no-daemon

# Use the Official OpenJDK image for a lean production stage of our multi-stage build.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM openjdk:21-jdk
ENV TZ="Europe/Prague"

# Copy the jar to the production image from the builder stage.
COPY --from=builder /home/gradle/build/libs/tt-cup-api-all.jar /tt-cup-api.jar
COPY ./admin.json /admin.json

# Run the web service on container startup.
CMD [ "java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/tt-cup-api.jar" ]