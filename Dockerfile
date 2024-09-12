FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar","-Duser.timezone=Asia/Seoul","--spring.profiles.active=dev"]
