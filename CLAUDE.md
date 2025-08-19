# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

"Site RH pour Postiers" - A complete Spring Boot HR management web application for postal workers with French interface, session-based authentication, and comprehensive HR features including personal data management, CV management, leave requests, attestations, transfers, and internal competitions.

## Common Commands

### Database & Infrastructure
- `docker-compose up -d postgres` - Start PostgreSQL database
- `docker-compose up -d` - Start all services (PostgreSQL + PgAdmin)
- `docker-compose down` - Stop all services

### Build and Run
- `./gradlew bootRun` - Run the Spring Boot application (with local profile)
- `./gradlew build` - Build the project
- `./gradlew clean` - Clean build artifacts

### Testing
- `./gradlew test` - Run all tests
- `./gradlew integrationTest` - Run integration tests with Testcontainers
- `./gradlew check` - Run all checks (tests, code quality)

## Architecture

### Domain Model (JPA Entities)
- **Utilisateur** - Users with matricule, email, roles (POSTIER/ADMIN)
- **DonneesPersonnelles** - Personal data with admin validation workflow
- **CV** - Resume management with PDF export capability  
- **DemandeAttestation** - Work/salary attestation requests
- **DemandeConge** - Leave requests with balance tracking
- **SoldeConges** - Leave balances by year
- **Concours** - Internal competitions
- **CandidatureConcours** - Competition applications

### Technology Stack
- **Backend**: Java 21, Spring Boot 3.3.5, Spring Security (sessions), Spring Data JPA
- **Database**: PostgreSQL 15+ with UUID primary keys
- **Frontend**: Thymeleaf, Bootstrap 5, vanilla JavaScript - ALL TEXT IN FRENCH
- **Security**: Form-based authentication with JSESSIONID sessions, BCrypt passwords
- **Build**: Gradle with Kotlin DSL
- **Testing**: JUnit 5, Testcontainers for integration tests

### Key Configuration
- **Security**: Session-based auth (no JWT), CSRF protection enabled
- **Database**: PostgreSQL with sample data (admin + 3 test users, password: "password123")
- **Profiles**: local (default), test, prod
- **Languages**: ALL user-facing content in French, code/packages in English

### Project Structure
```
src/main/java/com/example/postierhr/
├── config/          # Spring Security & Web configuration
├── controller/      # MVC controllers (French routes/responses)
├── dto/            # Data Transfer Objects with validation
├── entity/         # JPA entities with French field names
├── mapper/         # MapStruct mappers
├── repository/     # Spring Data repositories
├── service/        # Business logic services
└── exception/      # Custom exceptions

src/main/resources/
├── static/         # CSS, JS, images
└── templates/      # Thymeleaf templates (French interface)
  ├── fragments/    # Reusable template fragments
  ├── public/       # Login, registration pages
  ├── postier/      # Employee dashboard and features
  └── admin/        # Administrator interface
```

### Test Users (password: "password123")
- admin@laposte.fr (ADMIN role)
- jean.martin@laposte.fr (POSTIER role)
- sophie.bernard@laposte.fr (POSTIER role) 
- pierre.durand@laposte.fr (POSTIER role)

### Development Notes
- All user interface text is in French
- Package names and code are in English
- Uses UUID as primary keys throughout
- Session-based authentication (no JWT)
- Bootstrap 5 for responsive UI
- Custom CSS with La Poste color scheme