# Controle Notas NFSe API – IAM

Módulo de autenticação e autorização (Identity & Access Management) construído com Java 23 e Spring Boot 3.4.x, seguindo DDD, Clean Architecture e SOLID.

Suporta:
- Cadastro e login via CNPJ (Pessoa Jurídica) e CPF (Pessoa Física)
- Autenticação JWT (stateless)
- Autorização baseada em roles: `ADMIN`, `COMPANY`, `ACCOUNTANT`

---

## Índice
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Subir PostgreSQL (Docker Compose)](#subir-postgresql-docker-compose)
- [Executar a aplicação (Gradle)](#executar-a-aplicação-gradle)
- [Migrações (Flyway)](#migrações-flyway)
- [Endpoints](#endpoints)
- [Testar com curl](#testar-com-curl)
- [Estrutura do projeto (alto nível)](#estrutura-do-projeto-alto-nível)

---

## Arquitetura
Camadas conforme DDD + Clean Architecture:

- Domain
  - Entidades/VOs/Enums do domínio e portas de repositório (sem dependência de frameworks)
- Application
  - Use cases, DTOs, ports (`TokenProvider`, `PasswordHasher`, `AuthenticatedUserProvider`)
- Infrastructure
  - JPA (entities e repositórios Spring Data), Specifications, JWT, Security (filtro/Config), Flyway
- Interface/Delivery
  - REST controllers, requests/responses, mappers e tratamento global de erros

---

## Tecnologias
- Java 23
- Spring Boot 3.4.x (Web, Validation, Security, Data JPA, Flyway)
- PostgreSQL
- JJWT (JWT)

---

## Pré-requisitos
- Java 23 instalado (ou usar a toolchain do Gradle)
- Docker e Docker Compose instalados
- Porta `5432` livre para o PostgreSQL

---

## Configuração
Arquivo: `src/main/resources/application.properties`

Pontos de atenção:
- Ajuste `security.jwt.secret` para uma chave Base64 de 256 bits (ex.: `openssl rand -base64 32`)
- As credenciais do banco estão alinhadas ao `docker-compose.yml` deste projeto

---

## Subir PostgreSQL (Docker Compose)

Na raiz do projeto:

```bash
docker compose up -d
```

Verifique o status:

```bash
docker compose ps
```

Banco disponível em `localhost:5432` com:
- Base: `controle_notas_nfse`
- Usuário: `postgres`
- Senha: `postgres`

---

## Executar a aplicação (Gradle)

Opção 1 (dev):

```bash
./gradlew bootRun
```

Opção 2 (via JAR):

```bash
./gradlew clean build
java -jar build/libs/controle-notas-nfse-api-0.0.1-SNAPSHOT.jar
```

---

## Migrações (Flyway)
Ao iniciar, o Flyway executa as migrações e cria as tabelas `users`, `roles`, `users_roles`, populando `ADMIN`, `COMPANY`, `ACCOUNTANT`.

---

## Endpoints
Prefixo: `/api/auth`

- `POST /api/auth/register/company` – cadastra PJ (CNPJ) com role `COMPANY` e retorna JWT
- `POST /api/auth/register/accountant` – cadastra PF (CPF) com role `ACCOUNTANT` e retorna JWT
- `POST /api/auth/login` – login via email/CPF/CNPJ, retorna JWT e dados do usuário
- `GET /api/auth/me` – retorna dados do usuário autenticado

Health por role:
- `GET /api/admin/health` – apenas `ADMIN`
- `GET /api/company/health` – apenas `COMPANY`
- `GET /api/accountant/health` – apenas `ACCOUNTANT`

---

## Testar com curl
Exemplos rápidos (ajuste para seu cenário):

```bash
# Cadastro Company
curl -i -X POST "http://localhost:8080/api/auth/register/company" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Empresa XPTO LTDA",
    "email": "contato@xpto.com.br",
    "password": "Senha123",
    "confirmPassword": "Senha123",
    "cnpj": "11222333000181"
  }'

# Login por e-mail
curl -i -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"contato@xpto.com.br","password":"Senha123"}'

# Obter usuário autenticado (substitua <TOKEN>)
curl -i "http://localhost:8080/api/auth/me" \
  -H "Authorization: Bearer <TOKEN>"
```

Mais exemplos de `curl` podem ser encontrados na conversa desta entrega; posso fornecer uma Collection para Insomnia/Postman se desejar.

---

## Estrutura do projeto (alto nível)

```
src
└── main
    ├── java
    │   └── io/invoicextech/controlenotas/nfse/api
    │       ├── auth
    │       │   ├── domain (model, vo, repository)
    │       │   ├── application (dto, port, usecase, mapper)
    │       │   ├── infrastructure (persistence, security)
    │       │   └── interface_ (rest controllers, requests, responses, mapper)
    │       ├── common/rest (GlobalExceptionHandler)
    │       ├── admin/company/accountant (health controllers por role)
    └── resources
        ├── application.properties
        └── db/migration (Flyway)
```

---

## Notas
- Projeto configurado para sessão stateless com filtro JWT Bearer
- Para logs SQL mais verbosos, ajuste `spring.jpa.show-sql=true` e níveis de logging em `application.properties`
