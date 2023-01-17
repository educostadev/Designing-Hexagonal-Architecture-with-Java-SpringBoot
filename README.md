# Muti-Module Hexagonal Spring Boot Project
This project was inspired by the book [Design Hexagonal Architecture with Java](https://www.amazon.com.br/Designing-Hexagonal-Architecture-Java-change-tolerant/dp/1801816484) that explains how the Hexagonal Architecture works. I decided apply the concepts from the book using Spring Boot.

The POM file is configured to divide the spring dependencies in each module according to its responsability.

## Technology
- JDK 17
- Spring Boot 3.0.1
- Maven 3.8.6

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
  ├───►│   framework  ├───┘
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

### Framework
The Framework hexagon is the place where we assemble all the adapters required to make the hexagonal system.

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
Package the application
```
./mvnw clean package
```

Run the application by command line
```
java -jar ./bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar
```

Test the rest endpoint
```
curl --location --request POST 'localhost:8080/api' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "São Paulo",
"state": "SP"
}'
```