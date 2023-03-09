FROM gradle:7.5-jdk18 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:19

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

ENV DEMO_CSV_INPUT=/app/files/order-integration.csv
ENV DEMO_PROCESSED_ORDERS=/app/files/processed-orders.json
ENV DEMO_USER_SERVICE_URL=user:50054

ENTRYPOINT ["java","-jar","/app/demo.jar"]