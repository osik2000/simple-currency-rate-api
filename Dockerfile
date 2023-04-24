FROM openjdk:17-jdk-alpine
LABEL authors="Pawel Osinski"
COPY build/libs/dynatrace.nbp.task.backend-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/backend.jar"]
