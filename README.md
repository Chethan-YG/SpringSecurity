# Spring Security Database Integration

This project demonstrates the integration of Spring Security with a MySQL database, OAuth2 authentication for Google and GitHub, and email-based functionality. The backend uses Spring Boot, with Thymeleaf as a templating engine for the frontend.

## Features

- **OAuth2 Authentication**: Google and GitHub authentication using OAuth2.
- **Database Configuration**: Spring Data JPA with MySQL.
- **Email Configuration**: Sends emails via Gmail SMTP server.
- **Spring Boot Integration**: Easily manage backend services.
- **Thymeleaf Integration**: For dynamic HTML rendering.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- JDK 11 or later
- MySQL Database
- OAuth2 credentials for Google and GitHub
- Maven or Gradle (Maven is used in this project)

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Chethan-YG/SpringSecurity.git
cd SpringSecurity
```

### 2. Configure Database

To set up your MySQL database, create a database named security, then configure your application.properties file with the database URL and credentials. Make sure to use environment variables to secure sensitive information.

#### application.properties

#### Database Configuration
- spring.datasource.url=jdbc:mysql://localhost:3306/${DB_NAME:security}
- spring.datasource.username=${DB_USERNAME:root}
- spring.datasource.password=${DB_PASSWORD:yourpassword}
- spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
- spring.jpa.hibernate.ddl-auto=update

${DB_NAME}, ${DB_USERNAME}, and ${DB_PASSWORD} are environment variables.
Replace the yourpassword default with your actual database password through environment variables.

### 3. Configure OAuth2 (Google and GitHub)
In application.properties, add your OAuth credentials from Google and GitHub.

#### Google OAuth Configuration
- spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
- spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
- spring.security.oauth2.client.registration.google.scope=email,profile

#### GitHub OAuth Configuration
- spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
- spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
- spring.security.oauth2.client.registration.github.scope=email,profile

### 4. Run the Application
To run the application locally:

bash
```
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080.

### 5. Access the Application
Once the application is running:

You can log in using your Google or GitHub credentials.

- controller: Contains controllers for managing web requests.
- model: Contains entity classes for interacting with the database.
- repository: Contains interfaces for data access using Spring Data JPA.
- templates: Contains Thymeleaf templates for rendering views.
  
#### Dependencies
- Spring Boot: The main framework for building the application.
- Spring Security: For handling OAuth2 login and securing the application.
- Spring Data JPA: For database interaction with MySQL.
- Thymeleaf: For rendering HTML views.
- Spring Mail: For sending emails using Gmail SMTP
