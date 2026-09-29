# Contact Manager API

API REST para gerenciamento de contatos pessoais desenvolvida com **Java 17** e **Spring Boot 3.2**.

## Tecnologias

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA / Hibernate
- PostgreSQL / H2 (desenvolvimento)
- Bean Validation
- Maven

## Como Executar

```bash
# Clonar o repositório
git clone https://github.com/Gudoourado/contact-manager-api.git
cd contact-manager-api/contact-manager-api

# Executar com Maven
mvn spring-boot:run

# A API estará disponível em http://localhost:8080
# Console H2: http://localhost:8080/h2-console
```

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
