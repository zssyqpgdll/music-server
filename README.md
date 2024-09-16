# Music Server
This project is the back-end part of the Music system project, implemented using Spring Boot and MyBatis, and contains various third-party API calls and Spark Java calls.

## Technical architecture
**Framework**: Spring Boot

**Data persistence**: MyBatis

**Data processing**: Spark Java API

**Database**: Mysql

**Third-party API**: Integration of multiple third-party services

## Development environment setup
1. Open projects using IDEA
2. Refresh the Maven file to import all dependencies
3. Set the NAT_APP_URL attribute

## Run
main: recommendation_system\recommendationSystemApplication.java
The default port number is 8080

## Function overview
- **API Integration**: Call various third-party service apis
- **Data processing**: Use Spark Java for data analysis and processing
- **Business logic**: Implement the core business logic of the music recommendation system
- **Data persistence**: Use MyBatis to handle database interactions