#Build
FROM gradle:4.4-jdk8-alpine as build

USER root

ENV GRADLE_USER_HOME /opt
COPY docker/gradle-config $GRADLE_USER_HOME/

WORKDIR /opt

COPY build.gradle ./
COPY src ./src

RUN gradle clean build

#Run
FROM openjdk:8-jre-alpine as runtime

RUN mkdir -p /opt/app

WORKDIR /opt/app

COPY --from=build /opt/build/libs/opt.jar .

EXPOSE 8081
EXPOSE 8080

CMD java -jar opt.jar