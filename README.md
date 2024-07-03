# Hostfully Booking API

This is a RESTful webservice written with Spring Boot to perform booking operations like:

- [x] Create a booking
- [x] Update booking dates and guest details Cancel a booking
- [x] Rebook a canceled booking
- [x] Delete a booking from the system
- [x] Get a booking
- [] Create, update and delete a block

## Folder Structure

```
└─ .
   ├─ README.md
   ├─ mvnw
   ├─ mvnw.cmd
   ├─ pom.xml
   └─ src
      ├─ main
      │  ├─ java
      │  │  └─ com
      │  │     └─ hostfully
      │  │        └─ booking
      │  │           └─ api
      │  │              ├─ BookingApi.java
      │  │              ├─ application
      │  │              │  └─ rest
      │  │              │     ├─ BookingRestController.java
      │  │              │     ├─ PersonRestController.java
      │  │              │     ├─ request
      │  │              │     │  ├─ ChangeBookingDatesRequest.java
      │  │              │     │  └─ CreateBookingRequest.java
      │  │              │     └─ response
      │  │              │        └─ BookingResponse.java
      │  │              ├─ domain
      │  │              │  ├─ BaseEntity.java
      │  │              │  ├─ Person.java
      │  │              │  ├─ Place.java
      │  │              │  ├─ Reservation.java
      │  │              │  ├─ ReservationStatus.java
      │  │              │  ├─ ReservationType.java
      │  │              │  ├─ repository
      │  │              │  │  ├─ PersonDAO.java
      │  │              │  │  └─ ReservationDAO.java
      │  │              │  └─ services
      │  │              │     ├─ BlockService.java
      │  │              │     ├─ BookingService.java
      │  │              │     └─ adapter
      │  │              │        ├─ BlockAdapter.java
      │  │              │        └─ BookingAdapter.java
      │  │              └─ infrastructure
      │  │                 ├─ configuration
      │  │                 │  ├─ BeanConfiguration.java
      │  │                 │  ├─ ErrorResponse.java
      │  │                 │  └─ GlobalExceptionHandler.java
      │  │                 ├─ exception
      │  │                 │  ├─ BusinessException.java
      │  │                 │  └─ NotFoundException.java
      │  │                 └─ repository
      │  │                    ├─ PersonDAODatabase.java
      │  │                    └─ ReservationDAODatabase.java
      │  └─ resources
      │     ├─ application-prod.yaml
      │     ├─ application.yaml
      │     ├─ db
      │     │  └─ migration
      │     │     ├─ V1__CREATE_PERSON.sql
      │     │     └─ V2__CREATE_RESERVATIONS.sql
      │     ├─ static
      │     └─ templates
      └─ test
         ├─ java
         │  └─ com
         │     └─ hostfully
         │        └─ booking
         │           └─ api
         │              ├─ BookingApiTests.java
         │              ├─ integration
         │              │  └─ PersonDAOIntegrationTest.java
         │              └─ unit
         │                 ├─ PersonTest.java
         │                 └─ ReservationTest.java
         └─ resources
            ├─ application-test.yaml
            └─ database
               └─ person-data.sql

```

## Architecture Design Principles

The architecture design of this project is inspired by the Ports and Adapters pattern, also known as Hexagonal Architecture. While we have not adhered strictly to the model, we have tailored our implementation to focus on aspects that deliver the most value for a project of this scale.
Inside src/main/java we have the main package `com.hostfully.booking.api`

- Application:
  - In and Out ports for interaction with the API;
  - Models to represent API contracts;
- Domain:
  - Core models;
  - Business logic;
  - Business interfaces;
- Infrastructure
  - Repository adapters to handle external resources;
  - Frawework configurations;
  - Custom exceptions handling;

Inside src/main/test we have main package too `com.hostfully.booking.api`, but holding tests.

- Integration
  - Tests that depend on external resources like databases;
- Unit
  - Tests that try to focus on the domain entities logic and business logic;

## Tech Stack

**Back-end:** Java 21, Spring Boot, H2 (in-memory database), Flyway, Lombok, Junit5, Maven

## Running locally

Clone the project

```bash
  git clone https://github.com/calebemachado/booking-api.git
```

Install

```bash
  ./mvnw clean install
```

Run the app

```bash
    ./mvnw spring-boot:run
```

Run the tests

```bash
    ./mvnw test
```
