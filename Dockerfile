FROM openjdk:11-buster
COPY . .
#RUN chmod +x ./mvnw
#RUN ./mvnw package
#COPY target/VocabularyWordsService-0.0.1-SNAPSHOT.jar VocabularyWordsService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","target/BeerTimeTelegramBot-jar-with-dependencies.jar"]
EXPOSE 8080