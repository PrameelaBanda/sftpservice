FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./build/libs/sftpservice-0.0.1-SNAPSHOT.jar /usr/app/sftpservice.jar

WORKDIR /usr/app

ENTRYPOINT ["java","-jar","sftpservice.jar"]

EXPOSE 80 22