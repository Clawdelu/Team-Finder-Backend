ARG JAVA_VERSION=17
ARG MAVEN_VERSION=3.8.6

FROM maven:${MAVEN_VERSION}-amazoncorretto-${JAVA_VERSION} AS builder
COPY src /opt/app/src
COPY pom.xml /opt/app
# Maven command meaning: clean: cleans up target folder, package: creates .jar file with all dependencies
RUN mvn -f /opt/app/pom.xml clean package

#
# Preparing container to be ready to run application
#
FROM amazoncorretto:${JAVA_VERSION} AS runner
# Install security patches
RUN yum update -y --security

WORKDIR /opt/app
# Copy in ready .jar file
COPY --from=builder /opt/app/target/TeamFinderAPI*.jar /opt/app/team-finder-backend.jar
# Set CMD to run java application from prepared .jar file
CMD ["java", "-jar", "/opt/app/team-finder-backend.jar"]
