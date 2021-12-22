# spring-boot-com.sftp
Spring Boot SFTP File Transfer using JSch Java Library

### Prerequisites:-
* [1] SpringBoot Tool suite or any IDE framework for ease of use.
* [2] Gradle
* [3] Docker (Optional)

### How to start application ?
1. Execute `./gradlew bootRun` command to build and run app or use samples/sftpservice-0.0.1-SNAPSHOT.jar which is committed separately. 
2. Run the below command to start app.

java -jar sftpservice-0.0.1-SNAPSHOT.jar

### Use Swagger UI to test services
Swagger UI is available at http://localhost:8080/swagger-ui.html
Below are the sample request and response for the services,

* [1] To get product details,

  Http Method : Get

  URL : 
  http://localhost:8080/api/file/download/test.txt
  
  Response :
  {Successfully downloaded the file}

* [2] To add price details for the product,

  Http Method : Post

  URL : 
  http://localhost:8080/api/file/upload
  
  Request Body :
  file: Select Multipart file to upload
  
  Response :
  {"Successfully uploaded the file"}


