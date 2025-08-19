# Muti-Module Hexagonal Spring Boot Project
The Hexagonal Architecture was coined by [Alistair Cockburn’s](https://archive.is/5j2NI) originally named Ports & Adapters and today it is one of the most popular archecture to organize your code. 
This project was inspired by the book [Design Hexagonal Architecture with Java](https://www.amazon.com.br/Designing-Hexagonal-Architecture-Java-change-tolerant/dp/1801816484) that explains how the Hexagonal Architecture works. 
I decided apply the concepts from the book using Spring Boot.

The POM file is configured to divide the spring dependencies in each module according to its responsability. 

This project also uses [ArchUnit](https://www.archunit.org/) to enforce architecture rules see the [HexagonalArchtectureApplicationTest](./application/src/test/java/dev/educosta/application/HexagonalArchtectureApplicationTest.java) class.

## Technologies
- JDK 21
- Spring Boot 4.0.0-M1 (Milestone Release)
- Spring Framework 7.0.0-M7
- Maven 3.9.x (via Maven Wrapper)
- Hibernate 7.0.7.Final
- ArchUnit 1.3.0
- JUnit 5
- Mockito
- H2 Database (for testing)
- Docker & Docker Compose
- MySQL 8.x (default for local development)
- Spring Data JPA (Also see the [SpringDataJPA-Querydsl](https://github.com/educostadev/SpringDataJPA-Querydsl) project)

## Module dependencies
The following diagram depicts the module dependency inside the Hexagonal Architecture:

```
       ┌──────────────┐
  ┌───►│    domain    │◄──┐
  │    └──────────────┘   │
  │           ▲           │
  │           │           │
  │    ┌──────┴───────┐   │
  ├───►│  application │◄──┤
  │    └──────────────┘   │
  │                       │
  │    ┌──────────────┐   │
  ├───►│Infrastructure├───┘
  │    └──────────────┘
  │
  │    ┌──────────────┐
  └────┤   bootstrap  │
       └──────────────┘
```
This diagram was made with [asiiflow tool](https://asciiflow.com/#/).

### Domain
In the Domain hexagon, we assemble the elements responsible for describing the core
problems we want our software to solve. Entities, Value Objects, Services, and Specifications are DDD concepts you can apply here.

```
  ┌─────────────────┐
  │                 │
  │ ┌─────────────┐ │
  │ │ Entities    │ │
  │ └─────────────┘ │
  │                 │
  │ ┌─────────────┐ │
  │ │ Value Objec.│ │
  │ └─────────────┘ │
  │                 │
  │ ┌─────────────┐ │
  │ │   Services  │ │
  │ └─────────────┘ │
  │                 │
  └─────────────────┘
```


### Application
This hexagon expresses the software's user intent and features based on the Domain hexagon's business rules.

- `Use cases` are just interfaces describing what the software does. It represents the system's behavior 
  through application-specific operations and may interact directly with entities and other use cases.
- `Input ports` implements use cases By being a component that's directly
  attached to use cases, at the Application level, input ports allow us to implement software intent.
- `Output ports` Allows use cases to fetch data from external resources to achieve its goals. That's the role of output ports, which are represented as interfaces
  describing the operations.
```
 ┌─────────────────┐
 │                 │
 │ ┌─────────────┐ │
 │ │ Use Cases   │ │
 │ └─────────────┘ │
 │                 │
 │ ┌─────────────┐ │
 │ │ Input Ports │ │
 │ └─────────────┘ │
 │                 │
 │ ┌─────────────┐ │
 │ │ Output Ports│ │
 │ └─────────────┘ │
 │                 │
 └─────────────────┘
```

### Infrastructure
The Infrastructure (or Framework) hexagon is the place where we assemble all the adapters required to make the hexagonal system.

- `Input Adapters` The input adapter`s role is to deal with driving operation. Driving actors are the ones who send requests to the application.
- `Output Adapters` The output adapter's role is to deal with driven operations. Driven actors representing the external components accessed by the application.
```
 ┌─────────────────────┐
 │                     │
 │ ┌─────────────────┐ │
 │ │  Input Adapters │ │
 │ └─────────────────┘ │
 │                     │
 │ ┌─────────────────┐ │
 │ │ Output Adapters │ │
 │ └─────────────────┘ │
 │                     │
 └─────────────────────┘
```

### Bootstrap
The bootstrap is not part of the Hexagon architecture. It joins all others modules together to start the application.

## Class Diagram
Requests came from Web and reach the `Input Adapters` (Spring Controllers) that use dependency injection to create `Use Cases` instances and attend the application intent.
Use cases are implemented by `Input Ports` that using dependency injection create `OutputPort` instances to interact with infrastructure componentes.
Output ports are implemented by `Output Adapters` and have concrete implementations (like Spring JPA) for reaching infrastructure components like databases.
````
           [WEB]
             │ 
             │ access
             ▼
    ┌─────────────────┐
    │  MyInputAdapter │  Spring Controllers
    └─────┬───────────┘
          │ uses
          ▼
    ┌─────────────────┐
    │  <<MyUseCase>>  │ Interface - What the software does
    └─────────────────┘
          ▲
          │ implements
    ┌─────┴───────────┐
    │   MyInputPort   │ Concrete Implementation of Use Cases
    └─────┬───────────┘
          │ uses
          ▼
    ┌─────────────────┐
    │<<MyOutputPort>> │ Interface
    └─────────────────┘
          ▲
          │ implements
    ┌─────┴───────────┐
    │ MyOutputAdapter │ JPA, Kafka, Redis etc.
    └────────┬────────┘
             │ access
             ▼
         [DATABASE]
```` 


# Running the application

## Prerequisites

Before running the application, you need to set up the database. This project uses MySQL as the default database for local development.

## Database Setup

### Option 1: Using Docker Compose (Recommended)

The project includes a Docker Compose configuration for MySQL database with phpMyAdmin for easy database management.

**Start the database services:**
```bash
cd infrastructure/datastore-mysql
docker-compose up -d
```

This will start:
- **MySQL Database** on port `3306`
  - Database: `db`
  - Username: `user` 
  - Password: `P@ssw0rd`
- **phpMyAdmin** on port `8081` (http://localhost:8081)
  - Server Host: `db`
  - Username: `user`
  - Password: `P@ssw0rd`

**Stop the database services:**
```bash
cd infrastructure/datastore-mysql
docker-compose down
```

For more details, see the [datastore-mysql README](infrastructure/datastore-mysql/README.md).

### Option 2: Local MySQL Installation

If you prefer to use a local MySQL installation, update the connection details in `bootstrap/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

## Building and Running

### 1. Package the application
```bash
./mvnw clean package
```

### 2. Run the application
```bash
java -jar ./bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar
```

The application will start on port `6001` and automatically create the database schema.

## Testing the API

### Create a city
```bash
curl --location --request POST 'localhost:6001/api' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "São Paulo",
"state": "SP"
}'
```

**Response:**
```json
{"id":"61dd9be9-a203-40f1-8950-f22e964da942","name":"São Paulo","state":"SP"}
```

### Get a city by ID
```bash
curl --location --request GET 'localhost:6001/api/61dd9be9-a203-40f1-8950-f22e964da942'
```

### Delete a city by ID
```bash
curl --location --request DELETE 'localhost:6001/api/61dd9be9-a203-40f1-8950-f22e964da942'
```

Replace `61dd9be9-a203-40f1-8950-f22e964da942` with the actual ID returned from the creation endpoint.

# How to Help

If you want to help this project you can:

- Give a Github Star for this project
- Implement more Hexagonal Archtecture rules using ArchUnit. (Open a PR) 
- Use Java Platform Module System (JPMS)[[1]](https://jenkov.com/tutorials/java/modules.html)[[2]](https://github.com/tfesenko/Java-Modules-JPMS-CheatSheet/blob/master/README.md) to restrict the visibility of each module. (Open a PR) 