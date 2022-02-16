FROM public.ecr.aws/fed-dev/amazon/corretto-11:latest

WORKDIR /opt/app

ARG JAR_FILE=target/aws-spring-boot-cloudwatch.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]