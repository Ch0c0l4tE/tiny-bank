# tiny-bank


## Assumptions
- Inactive users can be fetched using Get by id method, but they dont appear on normal search
- All search methods are using cursor pagination instead offset
- It's only possible to execute transactions within existing accounts - this means that operations to external accounts
  that our system will be considered invalid, but the transaction resource is not tied to accounts by path to enable
  the possibility of implementing this introducing the minimum of breaking changes.
- When we deactivate a user all his accounts are also deactivated
- Deactivated accounts cant preform any type of transactions
- HAL (Hypertext Application Language) concept was introduced to all REST endpoints to improve api understanding.


## Application Design
- This applications takes benefit of Hexagonal Architecture concept, in order to make the structure following a 
known architecture pattern and with this improve some the non functional requirements ([article here](https://alistaircockburn.com/Hexagonal%20Budapest%2023-05-18.pdf))
as maintainability, scalability, flexibility, testability, etc.
- Brief summary of in what this translates in terms of project structure:
```
  src/
  └── main/
      ├── java/
      │   └── com/
      │       └── joao.costa.tynybank/
      │           ├── application/
      │           │   ├── usecases/ # Application service (business logic) - flow/orchestration/inter domain validations
      │           │   └── port/  # needed contracts to have success executing the usecaes
      │           ├── domain/ # Models  validation 
      │           └── adapters/
      │           │   └── in/  # components that expose business logic/features to the outside 
      │           │   └── out/  # components that implement port interfaces responsible for accessing outside resources (databases, external services, etc)
      │           └── config.di/ # Dependency injection config 
      └── resources/
            └── application.properties   # Configuration 
  └── test/ # unit tests
          
```

## Docs
- Open API spec [here](./docs/openapi.yml)
- Postman Collection [here](./docs/Tiny%20Bank.postman_collection.json)
- Brief domain [here](./docs/domain.drawio.png)

## TODO
[] users tests
[] accounts tests
[] transactions tests
[] api spec
[] epoch milis

## Pre Requirements
- Java 21 JDK
- Quarkus 3.17.5

## Running the application 
### Running the application in dev mode

```shell script
./mvnw quarkus:dev
```

### Running unit tests

```shell script
./mvnw quarkus:test
```

### Packaging and running the application

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/tiny-bank-1.0.0-SNAPSHOT-runner`


