# 🎵 Music Request API

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/JWT-Authentication-orange)](https://jwt.io/)
[![Docker](https://img.shields.io/badge/Docker-Container-blue)](https://www.docker.com/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Messaging-orange)](https://www.rabbitmq.com/)
[![Swagger](https://img.shields.io/badge/Swagger-Documentation-brightgreen)](https://swagger.io/)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)](https://junit.org/)

## 🎯 Problema

Atualmente, o meu gerenciamento de pedidos de músicas é feito manualmente via WhatsApp:

- Alunos solicitam músicas por mensagem
- Os pedidos são organizados manualmente
- O material é preparado e enviado individualmente

## 💡 Solução

Esta API foi desenvolvida para centralizar e automatizar o fluxo de pedidos de músicas:

- Registro estruturado de pedidos
- Controle de status (PENDING → PROCESSING → COMPLETED)
- Separação de responsabilidades entre alunos e professores
- Base para integração com um frontend web para solicitação de músicas
- Preparação para envio automatizado de materiais (ex: email)

## 🏗️ Arquitetura

- **Hexagonal (Ports & Adapters)** - Domínio isolado da infraestrutura
- **SOLID + Clean Code** - Código limpo e testável
- **Preparado para Microsserviços** - Estrutura modular

```text
├── domain/        # Modelos, enums, ports (interfaces)
├── application/   # Controllers, DTOs, services, mappers
└── infrastructure # JPA, Security, Docker configs
```

## 🛠️ Stack

| Categoria | Tecnologias |
|-----------|-------------|
| **Backend** | Java 17, Spring Boot 3.5, Spring Security, JWT, Spring Data JPA |
| **Banco de Dados** | PostgreSQL, H2 (desenvolvimento) |
| **Mensageria** | RabbitMQ, AMQP |
| **Testes** | JUnit 5, Mockito, @SpringBootTest, @DataJpaTest |
| **DevOps** | Docker, Docker Compose, Maven |
| **Documentação** | OpenAPI 3.0, Swagger UI |

## 🚀 Funcionalidades

| Funcionalidade | Descrição |
|----------------|-----------|
| **Autenticação JWT** | Login e token que expira em 24hrs |
| **Roles** | STUDENT / TEACHER |
| **CRUD** | Criar Pedido, Listar, Buscar e Atualizar Status|
| **Regras de Negócio** | Transições de Status Validadas |
| **Mensageria** | Eventos assíncronos com RabbitMQ |
| **Validações** | Bean Validation + Exceptions Personalizadas |
| **Tratamento Global** | @RestControllerAdvice com status HTTP |
| **Paginação** | Pageable do Spring Data |

## 📡 Endpoints Principais

| Método | Endpoint | Descrição | Role |
|--------|----------|-----------|------|
| POST | `/api/v1/auth/register` | Registrar usuário | Público |
| POST | `/api/v1/auth/login` | Login (retorna JWT) | Público |
| POST | `/api/v1/orders` | Criar pedido | STUDENT/TEACHER |
| GET | `/api/v1/orders` | Listar pedidos (paginado) | STUDENT/TEACHER |
| GET | `/api/v1/orders/{id}` | Buscar pedido | STUDENT/TEACHER |
| PATCH | `/api/v1/orders/{id}/status` | Atualizar status | TEACHER |

## 📚 Documentação da API

A API possui documentação interativa gerada com OpenAPI 3.0 (Swagger).

### Acesso
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Como usar a documentação
1. Acesse o Swagger UI
2. Use o endpoint `/auth/login` para obter um token JWT
3. Clique no botão **Authorize** e insira: `Bearer SEU_TOKEN`
4. Teste qualquer endpoint autenticado

## 🚀 Como Executar

```bash
# Local (H2)
./mvnw spring-boot:run

# Docker
docker-compose up -d --build   # Sobe PostgreSQL + Aplicação
```

- Profile dev → H2 em memória (desenvolvimento)
- Profile docker → PostgreSQL (produção)


## 🧪 Testes

```bash
# Testes Unitários
./mvnw test

# Testes de Integração
./mvnw verify
```

## 📊 Próximos Passos (Roadmap)

| Fase                         | Status        |
|------------------------------|---------------|
| Core + Autenticação JWT      | ✅ Concluído  |
| Testes Unitários/Integração  | ✅ Concluído |
| Mensageria (RabbitMQ)        | ✅ Concluído   |
| Documentação Swagger/OpenAPI | ✅ Concluído   |
| Cache (Redis)                | ⏳ Pendente   |
| Deploy em Cloud (AWS)        | ⏳ Pendente   |

## 📞 Contato
Desenvolvido por *João Paulo*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/joaowais/)
[![Instagram](https://img.shields.io/badge/Instagram-E4405F?style=flat&logo=instagram&logoColor=white)](http://www.instagram.com/joaowais)
