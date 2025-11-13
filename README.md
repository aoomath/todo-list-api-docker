## ToDo List API
API REST desenvolvida em Spring Boot 3.5.7 e Java 21 para gerenciamento de listas e tarefas. Inclui validação,
tratamento global de exceções, Swagger, Actuator, testes (unitários e integração com Testcontainers + Rest
Assured) e pipeline para gerar imagem Docker via GitHub Actions.

---
## Principais Características
- CRUD de Listas e Tarefas
- Validação com Bean Validation (Jakarta Validation)
- Tratamento centralizado de exceções (GlobalExceptionHandler)
- Documentação automática com Swagger / OpenAPI
- Health & Info via Spring Actuator
- Testes:
  - Unitários: JUnit 5 + Mockito
  - Integração: Testcontainers + PostgreSQL 17 + Rest Assured
- Docker + Docker Compose
- Pipeline GitHub Actions para build, testes e push de imagem no Docker Hub
---
## Como Executar Localmente
- Clone o repositório: git clone https://github.com/aoomath/todo-list-api-docker.git cd todo-list-api-docker
- Suba o banco com Docker Compose: docker-compose up -d
- Execute a aplicação: ./mvnw spring-boot:run
---
## Swagger & Actuator
- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator endpoints: /actuator/health, /actuator/info, /actuator/env, /actuator/metrics, /actuator/loggers
  
---
## Docker
- Build da imagem: docker build -t aoomath/todo-list-api-docker .
- Rodar com Docker Compose: docker-compose up -d
---
## GitHub Actions
- O pipeline automatizado executa: build e execução dos testes (unitários e integração), build da imagem Docker e
push automático para Docker Hub. Secrets necessários: DOCKERHUB_USERNAME e DOCKERHUB_TOKEN.
---
- Licença MIT — sinta-se à vontade para usar, modificar e contribuir.
- Autor Matheus A. Ferreira GitHub: https://github.com/aoomath
