# Spring-demo
Demo spring server to store messages and their hash (SHA256)


## Preconditions

Need to be installed:

[Java 11 sdk](https://openjdk.java.net/install/)  
Was tested with java 11, but should run also with java 8. For this need to change java.version property in [pom.xml](pom.xml))   
  
[Maven](https://maven.apache.org/download.cgi)

### How to use

After cloning:
- Define server port that you need inside [application.yml](src/main/resources/application.yml)
- run `mvn clean install` inside clonned folder
- run `java -jar target/spring-demo-0.0.1-SNAPSHOT.jar`

Server is up!
