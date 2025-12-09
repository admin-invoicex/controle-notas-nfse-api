Controle Notas NFSe API – IAM (Identity & Access Management)

Descrição
Módulo de autenticação e autorização construído com Java 23 e Spring Boot 3.4.x, seguindo DDD, Clean Architecture e SOLID. Suporta cadastro e login via CNPJ (Pessoa Jurídica) e CPF (Pessoa Física), autenticação JWT (stateless) e controle de acesso por roles (ADMIN, COMPANY, ACCOUNTANT).

Arquitetura (resumo)
- Domain: entidades/VOs/enums do domínio e portas de repositório (sem anotações de framework)
- Application: use cases, DTOs, ports (TokenProvider, PasswordHasher, AuthenticatedUserProvider)
- Infrastructure: JPA (entities e repositórios Spring Data), Specifications, JWT, Security (filtro/Config), Flyway
- Interface/Delivery: REST controllers, requests, responses, mappers e tratamento global de erros

Pré-requisitos
- Java 23 instalado (ou usar a toolchain do Gradle)
- Docker e Docker Compose instalados
- Porta 5432 livre para o PostgreSQL

Configuração
O arquivo src/main/resources/application.properties contém as propriedades necessárias. Ajuste especialmente a propriedade security.jwt.secret para uma chave Base64 de 256 bits.

Executando o PostgreSQL com Docker
1. Na raiz do projeto, execute: docker compose up -d
2. Verifique o status: docker compose ps
3. O banco ficará disponível em localhost:5432, com base controle_notas_nfse, usuário postgres e senha postgres.

Executando a aplicação com Gradle
Opção 1 (recomendado em dev):
- Executar: ./gradlew bootRun

Opção 2 (via JAR):
- Gerar o pacote: ./gradlew clean build
- Executar o JAR: java -jar build/libs/controle-notas-nfse-api-0.0.1-SNAPSHOT.jar

Migrações de banco (Flyway)
Ao iniciar a aplicação, o Flyway executará automaticamente as migrações, criando as tabelas users, roles, users_roles e populando os papéis iniciais (ADMIN, COMPANY, ACCOUNTANT).

Endpoints principais (prefixo /api/auth)
- POST /api/auth/register/company  cadastra usuário PJ (CNPJ) com role COMPANY e retorna JWT
- POST /api/auth/register/accountant  cadastra usuário PF (CPF) com role ACCOUNTANT e retorna JWT
- POST /api/auth/login  login por email ou CPF/CNPJ e retorno de JWT e dados do usuário
- GET /api/auth/me  retorna dados do usuário autenticado
Health endpoints protegidos por role
- GET /api/admin/health  ADMIN
- GET /api/company/health  COMPANY
- GET /api/accountant/health  ACCOUNTANT

Observações
- O projeto está configurado para stateless session e filtro JWT Bearer.
- Para logs SQL mais verbosos, ajuste spring.jpa.show-sql e níveis de logging no application.properties.
