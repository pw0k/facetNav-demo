##Build stage
#FROM gradle:jdk17 AS BUILD
#WORKDIR /usr/app/
#COPY . .
#RUN gradle build
#
## Package stage
#FROM openjdk:21-jdk-slim
#ENV JAR_NAME=app.jar
#ENV APP_HOME=/usr/app/
#WORKDIR $APP_HOME
#COPY --from=BUILD $APP_HOME .
#EXPOSE 8080
#ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME



FROM openjdk:21-jdk-slim

VOLUME /tmp
COPY /build/libs/facetnav-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]