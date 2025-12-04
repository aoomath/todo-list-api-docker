# 📝 ToDo List API — Spring Boot + Testcontainers

Uma API REST para gerenciamento de listas e tarefas, construída com **Spring Boot 3.5.7** e **Java 21**, incluindo validação, tratamento global de exceções, documentação automática com Swagger, Actuator, testes avançados e pipeline completo com Docker e GitHub Actions.

---

# ⚡ Visão Geral

Esta API fornece endpoints para gerenciar:

- 🗂️ Listas  
- ✅ Tarefas  

E conta com um conjunto robusto de infraestrutura:

- 🛠 Validação com Bean Validation
- 🚫 Tratamento global de erros
- 📚 Documentação com Swagger / OpenAPI
- 📊 Actuator para métricas e health check
- 🧪 Testes (unitários + integração com Testcontainers)
- 🐳 Docker / Docker Compose
- 🔁 Pipeline GitHub Actions com build + testes + push Docker

---

# 🏗️ Tecnologias Principais

| Categoria   | Tecnologias                           |
|-------------|----------------------------------------|
| Linguagem   | Java 21                                |
| Framework   | Spring Boot 3.5.7                      |
| Banco       | PostgreSQL 17 (Testcontainers)         |
| Testes      | JUnit 5, Mockito, Rest Assured         |
| DevOps      | Docker, Docker Compose, GitHub Actions |
| Docs        | Swagger / OpenAPI                      |

---

# ▶️ Como Rodar o Projeto

## 1️⃣ Clonar o repositório
```bash
git clone https://github.com/aoomath/todo-list-api-docker.git
cd todo-list-api-docker
```

## 2️⃣ Subir o PostgreSQL
```bash
docker-compose up -d
```

## 3️⃣ Iniciar a aplicação
```bash
./mvnw spring-boot:run
```

---

# 📘 Documentação e Monitoramento

### 🔍 Swagger  
http://localhost:8080/swagger-ui.html

### 📡 Actuator
- /actuator/health  
- /actuator/info  
- /actuator/metrics  
- /actuator/env  
- /actuator/loggers  

---

# 🐳 Docker

## Criar imagem
```bash
docker build -t aoomath/todo-list-api-docker .
```

## Subir com Docker Compose
```bash
docker-compose up -d
```

## Docker Hub
Imagem disponível em:  
[matheusferr/todolist](https://hub.docker.com/r/matheusferr/todolist)

---

# 🔁 CI/CD — GitHub Actions

O pipeline realiza automaticamente:

- Build  
- Testes (unitários + integração)  
- Build Docker  
- Push automático para Docker Hub  

### Secrets necessários

- DOCKERHUB_USERNAME  
- DOCKERHUB_TOKEN  

---

# 📄 Licença

Distribuído sob MIT License.

---

# ✨ Autor

Matheus A. Ferreira  
GitHub: https://github.com/aoomath
