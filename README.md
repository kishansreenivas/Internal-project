🎬 FlixShow - A Microservices-Based Movie Booking Platform
FlixShow is a scalable, distributed microservices-based like BookMyShow. It demonstrates modern enterprise software architecture built using Spring Boot, Spring Cloud, and a full suite of DevOps-ready tools.

🧩 Architecture Overview
Upload this diagram (project img.png) to the root of your project for the image to render in GitHub.

🚀 Overview
FlixShow allows users to:
Browse and book movies in different theaters
Manage payments, bookings, and notifications
Authenticate securely using JWT
Interact with resilient, discoverable microservices via an API Gateway

🛠 Used Tools & Purpose
Purpose	Project / Library
Microservices Communication	Spring Cloud OpenFeign / RESTTemplate
Config Management	Spring Cloud Config Server
Service Discovery	Spring Cloud Netflix Eureka / Consul
API Gateway	Spring Cloud Gateway
Database ORM	Spring Data JPA (Hibernate)
Authentication	Spring Security + JWT 
Scheduling	@Scheduled with Redis TTL
Rate Limiting / Circuit Breaker	Resilience4j / Spring Cloud Circuit Breaker
Dev Tools	Spring Boot DevTools, Lombok
Testing	JUnit5, Mockito, Testcontainers
Logging	Logback + slj4
API Docs	SpringDoc OpenAPI / Swagger

🔐 Authentication (JWT)
Login Endpoint:
POST http://localhost:9998/api-gateway/authenticate -> to get a tooken

How to Use:
Pass user credentials to receive a JWT token.
Use the token for all protected endpoints:

Authorization: Bearer <your_token>
⚙️ Configuration Server
All service configurations are externalized and managed via a centralized Spring Cloud Config Server.

📁FILE 
🔐 Authentication (JWT + File-Based Validation)
Endpoint:
POST http://localhost:9998/api-gateway/authenticate

How it works:
API Gateway reads from a file (/FILE/ceate.txt) containing allowed usernames.
If the provided username exists in the file, a JWT token is issued.
Use this token in the Authorization header for all protected routes.

📁 Config Repo:
CONFIG_SERVER on GitHub


🧱 Microservices List
👤 User-Service
🎥 Movie-Service
🧾 Booking-Service
💳 Payment-Service
📢 Notification-Service
🌐 API-Gateway
🔎 Eureka Server
⚙️ Config Server


⚡ Installation & Running
Java 21+
Maven
Redis, MySQL
STS or IntelliJ IDE
Steps
Clone the repository
Set up MySQL schema for each service and configure application.yml

Start:
Config Server
Eureka Server
API Gateway
Each Microservice (can be run independently)
Use Postman or Frontend to test APIs

📄 API Documentation
All services have auto-generated Swagger docs via SpringDoc OpenAPI.

URL Pattern:

http://localhost:<port>/swagger-ui.html
📂 Folder Structure
flixshow/
├── api-gateway/
├── config-server/
├── discovery-server/
├── user-service/
├── movie-service/
├── booking-service/
├── payment-service/
├── notification-service/
└── common-utils/

✅ Features
JWT-based Authentication
API Gateway Routing
Service Discovery with Eureka
Circuit Breaker with Resilience4j
Centralized Config via Spring Cloud Config
Redis Caching & Scheduling
API Documentation via Swagger
Monitoring with Actuator and optional ELK

🧪 Testing
✅ JUnit 5
✅ Mockito
✅ Springboottest

🤝 Contributing
Fork the repository
Create your feature branch: git checkout -b feature-name
Commit your changes
Push to the branch
Open a Pull Request

🙋‍♂️ Author
Sumeet Khandale
🔗 GitHub
