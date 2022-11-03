FROM alpine/git AS git
RUN git clone https://github.com/andreagia/nmrdbexcel.git

FROM maven AS builder
COPY --from=git  /nmrdbexcel /nmrdbexcel
RUN cd nmrdbexcel; mvn clean package -DskipTests

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder /nmrdbexcel/target/*.jar application
#ARG JAR_FILE=*.jar
#COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]