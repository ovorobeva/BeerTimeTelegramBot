FROM maven:3.8.2-openjdk-11-slim
COPY . .
LABEL maintainer="passant.dlm@gmail.com"
RUN mvn clean compile assembly:single
ENTRYPOINT ["java","-jar","target/BeerTimeTelegramBot-jar-with-dependencies.jar"]
EXPOSE 8000

