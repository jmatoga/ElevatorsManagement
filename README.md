# Elevators Management

The Elevators Management Application is a robust system designed with a Spring Boot backend and a React frontend. This application offers a comprehensive suite of RESTful API endpoints, facilitating efficient management and monitoring of elevator operations.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- SQL (H2 database)
- Maven
- React
- Bootstrap

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

First clone repo.

Second start Spring Boot application.

Third install components `npm install` in React and then `npm start`

Access the application in your browser at `http://localhost:3000`.

Server is on port `8090`.

### To log in choose one of existing users:

- `wrogal@example.com` `Admin123!`
- `jwisniewski@example.com` `Admin123!`
- `awesolowska@example.com` `Admin123!`
- `mkolonko@example.com` `Admin123!`
- `enaczelna@example.com` `Admin123!`

## Algorithm 
- If there are >=3 people in the elevator, the next one will not enter and will receive a notification about it.
- If there is no one in the elevator and the elevator is on a higher floor than 0, it will go down to floor 0 by itself
- The elevator step (travel time between one floor and another) is 5 seconds
- Before each step of the elevator, the order of floors at which the elevator is to stop is set. At the beginning, the algorithm sets them in the order in which they are closest, i.e.:
  - if the elevator is currently on the 2nd floor and users clicked: 4,1,3,0 and the elevator has direction UP, it will be set: 2 (current), 3, 4, 1, 0
- the estimated waiting time is also checked (number of floors to travel), i.e.:
  - if the elevator is currently on the 1st floor and the user clicked on floor 4 and the elevator will reach this floor and the user on floor 0 clicks the button and then the user who just got in says he wants to go to floor 5, then the elevator will go to floor 5 first because he has shorter distance there

- The algorithm is located in TaskService.java from line 44
- The program works regardless of the number of users logged in at once

## TO DO
- possibility to edit user data
- register new user, accept code sending by mail
- use roles eg. admin should disable elevator for repair
- write tests with jUnit and Mockito
