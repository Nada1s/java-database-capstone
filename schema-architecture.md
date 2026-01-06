# System Architecture

## Architecture Summary

This Spring boot application uses both **MVC** and **REST controllers**.
**Thymeleaf templates** are used for the **Admin** and **Doctor** dashboards, while **REST APIs** serve all other modules.
The application interacts with two databases: **MySQL** for **patient, doctor, appointment, and admin** data and **MongoDB** for **prescriptions**. 
All controllers route requests through a **common service layer**, which in turn delegates to the appropriate repositories.
**MySQL** access is handled using **JPA/Hibernate**, while **MongoDB** uses **document-based models**.

## Data and Control Flow

1. The user accesses the Admin or Doctor dashboard or interacts with a REST-based module.
2. The requested action is routed to the appropriate **Thymeleaf (MVC) controller** or **REST controller**.
3. The controller delegates request handling to the **Service layer**.
4. The Service layer applies business logic and determine the required data source.
5. The Service layer invokes the appropriate **repository** (MySQL JPA repository or MongoDB repository).
6. The repository interacts with the corresponding **database** (MySQL or MongoDB) and returns the data to the Service layer.
7. The Service layer returns the processed result to the controller, which responds with either a **rendered HTML view** or a **JSON response** to the user.
