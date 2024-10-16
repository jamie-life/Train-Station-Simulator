# Train Station Simulator

This is a Java-based Train Station Simulation project, organized as a multi-module Maven application. The project includes components for simulating a metro station system and handling user authentication. Each module has its own responsibilities and can be built and run independently.

## Table of Contents
- [Project Structure](#project-structure)
- [Features](#features)
- [Usage](#usage)
- [Modules Overview](#modules-overview)
  - [Metro Module](#metro-module)
  - [User Authentication Module](#user-authentication-module)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Project Structure

## Features

- **User Module**:
  - Manage metro stations, transactions, and fare calculation.
  - DTOs for various entities such as transactions, users, and stations.

- **User Authentication Module**:
  - User registration, login, and authentication with JWT tokens.
  - Secure user management system using role-based access control.

## Usage

Once the application is running, you can use the following endpoints:

- **User Module**:
  - `/api/user/get-transactions`: Gets User Train Journey Transactions.
  - `/api/user/add-funds`: Add funds to User Balance.
  - `/api/user/add-journey`: Add Train Journey Transaction.

- **Authentication Module**:
  - `/auth/register`: Register a new user.
  - `/auth/login`: Login and retrieve a JWT token.
  - `/logout`: Login and retrieve a JWT token.

## Modules Overview

### Metro Module

The **Metro Module** is responsible for handling station operations, including:
- **Transaction Handling**: Process transactions like top-ups and fare deductions.
- **DTOs**: Data transfer objects for handling user input and responses, such as `TransactionDto`, `UserDto`, etc.
- **Controllers**: REST API controllers like `UserController` for user-related operations.

### User Authentication Module

The **User Authentication Module** is responsible for:
- **User Registration and Authentication**: Handle JWT-based authentication.
- **Security Configuration**: Managed by the `SecurityUtils` and other related classes.
- **Services**: `UserService` and `AuthenticationService` for user management.

## Technologies Used

- **Java 11+**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Maven** (for dependency management and build)
- **REST API**
