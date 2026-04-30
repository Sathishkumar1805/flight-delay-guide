# flight-delay-guide

A monorepo containing the backend (Spring Boot) and frontend (Angular) for the Flight Delay Guide application.

## Structure

```
flight-delay-guide/
├── backend/    # Spring Boot API
├── frontend/   # Angular app
├── .env.example
└── .gitignore
```

## Getting Started

1. Copy `.env.example` to `.env` and fill in your API keys.
2. Copy `backend/src/main/resources/application-local.yml.example` to `application-local.yml` and add real keys.
3. Run the backend: `cd backend && mvn spring-boot:run`
4. Run the frontend: `cd frontend && npm install && ng serve`
