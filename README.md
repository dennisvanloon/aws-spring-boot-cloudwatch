# Step 1 - Create a simple REST endpoint

Create a simple spring boot application using Springboot CLI.
spring init --dependencies=data-rest spring-boot-simple

If needed, update the java version in the pom. I changed it to java 8.

Add the most simple RestController that just greets the user.

Build it: `./mvnw clean install`

Run it: `./mvnw spring-boot:run`

Access it: `http://localhost:8080/`

Add a test that calls the RestController one time and verifies the response.

docker build -t aws-spring-boot-cloudwatch:latest .
docker run -p 8080:8080 -e AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY -e AWS_SESSION_TOKEN -t aws-spring-boot-cloudwatch:latest

