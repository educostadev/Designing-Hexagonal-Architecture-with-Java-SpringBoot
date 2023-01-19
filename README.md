# Muti-Module Hexagonal Spring Boot Project
The Hexagonal Architecture was coined by [Alistair Cockburn’s](https://archive.is/5j2NI) originally named Ports & Adapters and today it is one of the most popular archecture to organize your code. 
This project was inspired by the book [Design Hexagonal Architecture with Java](https://www.amazon.com.br/Designing-Hexagonal-Architecture-Java-change-tolerant/dp/1801816484) that explains how the Hexagonal Architecture works. 
I decided apply the concepts from the book using Spring Boot.

The POM file is configured to divide the spring dependencies in each module according to its responsability. 

This project also uses [ArchUnit](https://www.archunit.org/) to enforce architecture rules see the [HexagonalArchtectureApplicationTest](./application/src/test/java/dev/educosta/application/HexagonalArchtectureApplicationTest.java) class.

## Technology
- JDK 17
- Spring Boot 3.0.1
- Maven 3.8.6
- ArchUnit
- Docker & Docker Compose

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

Start the Database using docker-compose. See how [here](infrastructure/datastore-mysql/README.md)

Package the application
```
./mvnw clean package
```

Run the application by command line
```
java -jar ./bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar
```

Test the creation endpoint endpoint
```
curl --location --request POST 'localhost:6001/api' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "São Paulo",
"state": "SP"
}'

RESPONSE
{"id":"61dd9be9-a203-40f1-8950-f22e964da942","name":"São Paulo","state":"SP"}
```

Test the read and delete endpoint. Take the returned ID from the above endpoint
```
curl --location --request GET 'localhost:6001/api/61dd9be9-a203-40f1-8950-f22e964da942'
curl --location --request DELETE 'localhost:6001/api/61dd9be9-a203-40f1-8950-f22e964da942'
```

# How to Help

If you want to help this project you can:

- Give a Github Star for this project
- Implement more Hexagonal Archtecture rules using ArchUnit. (Open a PR) 
- Use Java Platform Module System (JPMS)[[1]](https://jenkov.com/tutorials/java/modules.html)[[2]](https://github.com/tfesenko/Java-Modules-JPMS-CheatSheet/blob/master/README.md) to restrict the visibility of each module. (Open a PR) 