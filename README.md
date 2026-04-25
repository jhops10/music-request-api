# 🎵 Music Request API

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/JWT-Authentication-orange)](https://jwt.io/)
[![Docker](https://img.shields.io/badge/Docker-Container-blue)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)](https://www.postgresql.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-red)](https://en.wikipedia.org/wiki/Hexagonal_architecture)

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

## 📌 Visão Geral

API REST para gestão de solicitações de músicas com autenticação JWT e controle de acesso baseado em roles (STUDENT/TEACHER).

O sistema implementa arquitetura hexagonal (Ports & Adapters), separando regras de negócio da infraestrutura, e inclui boas práticas como validação de dados, tratamento global de exceções e autenticação stateless.

### 🔧 Stack Principal
| Tecnologia | Aplicação |
|-----------|----------|
| Java 17 / Spring Boot 3.2 | Core do backend |
| Spring Security + JWT | Autenticação stateless |
| Spring Data JPA + Hibernate | Persistência |
| PostgreSQL / H2 | Banco de dados |

### 🧪 Testes
| Tecnologia | Aplicação |
|-----------|----------|
| JUnit 5 | Testes unitários |
| Mockito | Mocks e isolamento de dependências |

### ⚙️ DevOps
| Tecnologia | Aplicação |
|-----------|----------|
| Docker + Docker Compose | Containerização |
| Maven | Build |

## 🧩 Arquitetura 
- **Hexagonal (Ports & Adapters)** - Domínio isolado da infraestrutura
- **SOLID + Clean Code** - Código limpo e de fácil manutenção
- **Preparado para Microsserviços** - Estrutura modular

## 📁 Estrutura (Arquitetura Hexagonal)

```text
├── domain/        # Modelos, enums, ports (interfaces)
├── application/   # Controllers, DTOs, services, mappers
└── infrastructure # JPA, Security, Docker configs
```

## 🚀 Funcionalidades

| Funcionalidade | Descrição |
|----------------|-----------|
| Autenticação JWT | Login e geração de tokens |
| Roles (STUDENT/TEACHER) | Controle de acesso granulado |
| CRUD de pedidos | Criar, listar, buscar, atualizar status |
| Regras de negócio | Transições de status (PENDING → PROCESSING → COMPLETED) |
| Validações | Bean Validation + exceptions personalizadas |
| Tratamento global | @RestControllerAdvice com status HTTP apropriados |

## 📡 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|----------|
| POST | /api/v1/auth/register | Registrar Usuário |
| POST | /api/v1/auth/login | Autenticar e Gerar JWT |
| POST | /api/v1/orders | Criar Pedido |
| GET | /api/v1/orders | Listar Pedidos (paginado) |
| GET | /api/v1/orders/{id} | Buscar Pedido por ID |
| PATCH | /api/v1/orders/{id}/status | Atualizar Status (TEACHER) |

## 🧪 Exemplos de Uso

### 🔐 Login

**POST /api/v1/auth/login**

#### Request
```json
{
  "email": "exemplo@email.com",
  "password": "senha_segura"
}
```

#### Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "email": "exemplo@email.com",
  "role": "STUDENT"
}
```

#### 🎵 Criar Pedido

**POST /api/v1/orders**

#### Request
```json

{
  "instrument": "VIOLA_CAIPIRA",
  "tone": "E",
  "musicName": "Música de Exemplo",
  "instructions": "Escrever o material de uma versão simplificada"
}
```

## 🐳 Docker

```bash
docker-compose up -d --build   # Sobe PostgreSQL + Aplicação
```

- Profile dev → H2 em memória (desenvolvimento)
- Profile docker → PostgreSQL (produção)

## 🧠 Conceitos Aplicados

### Arquitetura
- Arquitetura Hexagonal (Ports & Adapters)
- Separação de Responsabilidades

### Backend
- DTOs e Mappers
- Paginação (Pageable)
- Validações com Bean Validation
- Tratamento de Exceções

### Infraestrutura
- Dockerização Completa


## 📊 Próximos Passos (Roadmap)

| Fase                         | Status        |
|------------------------------|---------------|
| Core + Autenticação JWT      | ✅ Concluído  |
| Testes Unitários/Integração  | ✅ Concluído |
| Mensageria (RabbitMQ)        | ✅ Concluído   |
| Documentação Swagger/OpenAPI | ✅ Concluído   |
| Cache (Redis)                | ⏳ Pendente   |
| Deploy em Cloud (AWS)        | ⏳ Pendente   |

## 🚀 Como Executar

```bash
# Local (H2)
./mvnw spring-boot:run

# Docker (PostgreSQL)
docker-compose up -d --build
```

## 📞 Contato
Desenvolvido por *João Paulo*
- [Linkedin](https://www.linkedin.com/in/joaowais/)
- [Instagram](http://www.instagram.com/joaowais)
