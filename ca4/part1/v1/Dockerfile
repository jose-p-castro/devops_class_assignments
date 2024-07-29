FROM openjdk:17-jdk-slim
LABEL authors="José Castro"

WORKDIR /app

COPY ./ca2/part1 .

RUN chmod +x gradlew

RUN ./gradlew build

CMD ["./gradlew", "runServer"]