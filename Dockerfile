# المرحلة 1: بناء الملف
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# إضافة العلم -Dstart-class لتأكيد مكان ملف التشغيل
RUN mvn clean package -DskipTests -Dstart-class=com.lab.captcha.CaptchaApplication

# المرحلة 2: التشغيل
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
