FROM openjdk:21-jdk-slim
LABEL authors="José Castro"

WORKDIR /app

COPY ./ca2/part1/build/libs/*.jar .

CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]