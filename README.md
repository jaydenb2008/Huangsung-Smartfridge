# CISC191
Intermediate Java Programming Final Project Template
## Prerequisites
1. Maven
2. Git
3. JDK 24
## Building
mvn clean install
## Running
### Server
mvn spring-boot:run
### JFXClient
mvn javafx:run
### WebClient
mvn spring-boot:run
## Common Module
Shared classes between Server, JFXClient, and WebClient modules.
## Server Module
The REST API server that exposes CRUD endpoints to clients and persists to database.
## JFXClient Module
The JavaFX client application used to interact with the server via REST client.
## WebClient Module
The spring web client application used to interact with the server via REST client. Uses Thymeleaf templates for dynamic html pages.
