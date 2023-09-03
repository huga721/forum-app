# 🚀 About the project
Forum App is a REST API created using the Spring Boot framework with an MVC architecture. The API provides endpoints for creating and managing users, categories, topics, comments, likes, reports, and user warnings. The project implements custom exceptions, logs, business logic, test containers and is secured with JWT authentication, endpoints are also secured and they require authorization by the user. 

# 🐳 How to run with Docker
**Ensure you have Docker installed on your device!**
- Clone the repository using link: ```https://github.com/huga721/forum-app.git```
- Generate the JAR following maven command ```mvn clean package -DskipTests```
- Open your terminal in the project directory and enter: ```docker compose up```
### Access Application Swagger documentation at: ```127.0.0.1:8085/swagger-ui.html```

# 💻 How to Run Locally
- Download or clone repositroy with link: ```https://github.com/huga721/forum-app.git```
- Open project with your favorite IDE
- Run ```ForumApplication.java``` with ```dev``` profile

### Exposed ports: 
- MySQL: ```127.0.0.1:3306```
### MySQL Login & Password: 
- Username: ```admin```
- Password: ```admin```
### Access Application Swagger documentation at: ```127.0.0.1:8080/swagger-ui.html```

### 🔑 IMPORTANT: Be sure you have created ```forum-app``` schema in Your manualy

# 🛠️ Stack
The Forum App REST API is built on the following cutting-edge technologies:
- Java 17
- Maven
- MySQL
- SpringDoc OpenApi
- Lombok
- Spring Boot
- Spring Data JPA
- Spring Security
- Jwt
- Junit
- Mockito
- Test Containers
- Spring Web MVC
- Liquibase
