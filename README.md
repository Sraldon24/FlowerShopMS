# 🌸 FlowerShopMS

A **distributed e-commerce application** for a fictional flower shop, built as part of the **Microservices course (Winter 2025)** at Champlain College.

This project demonstrates **modern microservices architecture** with real-world deployment practices, integrating multiple services, databases, and tools.

---

## 🚀 Features

- **Microservices Architecture**
  - Independent services for **Orders**, **Inventory**, and **Customer Management**.
  - Designed with **Domain-Driven Design (DDD)**, **DTO**, and **Repository patterns**.

- **Service Communication**
  - RESTful APIs with **Spring Boot** and **HATEOAS**.
  - Clear separation of concerns between services.

- **Databases**
  - **PostgreSQL** → relational data (customers, orders, employees).
  - **MongoDB** → unstructured data (catalogs, reviews).
  - **H2** → lightweight testing.

- **Deployment & DevOps**
  - Containerized with **Docker** and orchestrated via **docker-compose**.
  - Automated **schema & data initialization** at startup.
  - Version control and collaboration with **Git/GitHub**.

- **Development Practices**
  - **MapStruct** for object mapping.
  - **Lombok** for reducing boilerplate.
  - **JUnit** & **Mockito** for unit testing.
  - Agile workflow with GitHub Projects (sprints & backlog).

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Web, Spring Data
- **Databases:** PostgreSQL, MongoDB, H2 (testing)
- **DevOps:** Docker, Docker Compose, GitHub
- **Testing:** JUnit, Mockito
- **Other Tools:** MapStruct, Lombok, Git

---

## 📖 Learnings

This project provided hands-on experience with:

Designing scalable microservices systems.

Managing multi-database integration.

Using containerization and orchestration for real-world deployment.

Applying Agile Scrum methodology in a team project.

---

## ⚡ Getting Started

### Prerequisites
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Run Locally
# Clone repository
git clone https://github.com/yourusername/FlowerShopMS.git
cd FlowerShopMS

# Start containers
docker-compose up --build




