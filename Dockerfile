FROM openjdk:17
ARG JAR_FILE=build/libs/inhub-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_ACTIVE} -jar /app.jar"]
