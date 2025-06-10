FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/member-service-0.0.1-SNAPSHOT.jar app.jar

# :흰색_확인_표시: ENTRYPOINT로 java만 실행
ENTRYPOINT ["java", "-jar", "app.jar"]