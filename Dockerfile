FROM openjdk:8-jre-alpine

EXPOSE 3000
WORKDIR /usr/src/ometria.froyo

COPY target/server.jar server.jar

CMD ["java", "-jar", "server.jar"]
