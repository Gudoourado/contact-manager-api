# Contact Manager API

[![CI](https://github.com/Gudoourado/contact-manager-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Gudoourado/contact-manager-api/actions/workflows/ci.yml)

API REST para gerenciamento de contatos pessoais desenvolvida com **Java 17** e **Spring Boot 3.2**.

## Tecnologias

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA / Hibernate
- PostgreSQL (H2 em memória no perfil `dev`)
- Bean Validation
- Maven

## Como Executar

Pré-requisitos: Java 17 ou mais recente e Maven.

```bash
git clone https://github.com/Gudoourado/contact-manager-api.git
cd contact-manager-api/contact-manager-api
```

### Rápido, sem instalar banco (perfil `dev`)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Usa um banco H2 em memória: sobe em segundos e os dados somem quando a aplicação para.

- API: http://localhost:8080
- Console do H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:contactdb`, usuário `sa`, sem senha)

### Com PostgreSQL

Crie o banco `contactdb` e rode:

```bash
mvn spring-boot:run
```

Usuário e senha vêm das variáveis `DB_USERNAME` e `DB_PASSWORD` (padrão: `postgres` / `postgres`).

## Testes

```bash
mvn test
```

Testes da camada web (`@WebMvcTest`, com o serviço simulado) que conferem o status HTTP e a mensagem de cada tipo de erro: JSON mal formado, parâmetro inválido ou ausente, método e content-type errados, endereço inexistente, validação e erro inesperado sem expor detalhe interno.

## Endpoints

| Método | Endpoint                      | Descrição                    |
|--------|-------------------------------|------------------------------|
| GET    | /api/contacts                 | Listar todos os contatos     |
| GET    | /api/contacts/{id}            | Buscar contato por ID        |
| POST   | /api/contacts                 | Criar novo contato           |
| PUT    | /api/contacts/{id}            | Atualizar contato            |
| DELETE | /api/contacts/{id}            | Deletar contato              |
| GET    | /api/contacts/search?keyword= | Busca geral por palavra-chave|
| GET    | /api/contacts/search/name?name=| Busca por nome              |

## Exemplo de Request (POST)

```json
{
  "name": "Gustavo Dourado",
  "email": "gustavo@email.com",
  "phone": "(71) 99618-0177",
  "address": "São Paulo, SP",
  "company": "Tech Company",
  "notes": "Desenvolvedor Backend"
}
```

## Estrutura do Projeto

```
src/main/java/com/gustavo/contactmanager/
├── ContactManagerApplication.java
├── controller/
│   └── ContactController.java
├── dto/
│   └── ContactDTO.java
├── exception/
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   └── Contact.java
├── repository/
│   └── ContactRepository.java
└── service/
    └── ContactService.java
```

## Autor

**Gustavo Dourado** - Desenvolvedor Backend Jr
