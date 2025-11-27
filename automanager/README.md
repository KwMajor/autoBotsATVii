# Autobots - Sistema de Gerenciamento de Clientes

## 📋 Sobre o Projeto

Autobots é um microserviço RESTful desenvolvido em Spring Boot para gerenciamento completo de dados de clientes de uma oficina automotiva. O sistema implementa CRUD completo com **HATEOAS**, **Bean Validation**, documentação **Swagger/OpenAPI**, e segue as melhores práticas de desenvolvimento de APIs REST.

### ✨ Funcionalidades

- ✅ **CRUD Completo** - Operações de criação, leitura, atualização e exclusão
- ✅ **HATEOAS** - Hypermedia as the Engine of Application State (links de navegação)
- ✅ **Bean Validation** - Validação automática de dados com anotações JSR-303
- ✅ **Swagger/OpenAPI** - Documentação interativa completa da API
- ✅ **Persistência JPA** - Hibernate com relacionamentos complexos
- ✅ **H2 Database** - Banco de dados em memória para desenvolvimento
- ✅ **Arquitetura em Camadas** - Controllers, Services, Repositories, Entities

### 🛠️ Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 2.6.3** - Framework para desenvolvimento de aplicações Java
- **Spring Data JPA** - Persistência de dados com JPA/Hibernate
- **Spring HATEOAS** - Implementação de hypermedia links
- **Bean Validation (JSR-303)** - Validação de dados com anotações
- **H2 Database** - Banco de dados em memória para desenvolvimento
- **Lombok 1.18.34** - Redução de código boilerplate
- **Springdoc OpenAPI 1.7.0** - Documentação automática da API (Swagger)
- **Maven** - Gerenciamento de dependências e build

### 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
├── controles/          # Controllers REST (endpoints da API)
├── entidades/          # Entidades JPA (modelo de dados)
├── repositorios/       # Repositories Spring Data JPA
└── modelo/             # Classes auxiliares (Select, Atualizador, Verificadores)
```

### 📦 Entidades Principais

- **Cliente**: Dados principais do cliente (nome, nome social, data de nascimento, data de cadastro)
  - Validações: Nome obrigatório (3-100 caracteres), data de nascimento no passado
  - Relacionamentos: OneToMany com Documentos e Telefones, OneToOne com Endereço
  
- **Documento**: Documentos do cliente (CPF, RG, CNH, etc.)
  - Validações: Tipo e número obrigatórios, número único
  - Relacionamentos: ManyToOne com Cliente
  
- **Endereco**: Endereço residencial do cliente
  - Validações: Cidade, rua e número obrigatórios, estado com 2 caracteres
  - Relacionamentos: OneToOne com Cliente
  
- **Telefone**: Telefones de contato do cliente
  - Validações: DDD (2-3 dígitos) e número (8-9 dígitos) obrigatórios, apenas números
  - Relacionamentos: ManyToOne com Cliente

### 🔗 HATEOAS - Hypermedia Links

Todos os endpoints retornam links de navegação seguindo o padrão HATEOAS:

```json
{
  "id": 1,
  "nome": "João Silva",
  "_links": {
    "self": {
      "href": "http://localhost:8080/cliente/1"
    },
    "clientes": {
      "href": "http://localhost:8080/cliente"
    }
  }
}
```

### ✅ Validações Implementadas

**Cliente:**
- `@NotBlank` - Nome é obrigatório
- `@Size(min=3, max=100)` - Nome deve ter entre 3 e 100 caracteres
- `@Past` - Data de nascimento deve ser no passado

**Documento:**
- `@NotBlank` - Tipo e número são obrigatórios

**Endereço:**
- `@NotBlank` - Cidade, rua e número são obrigatórios
- `@Size(min=2, max=2)` - Estado deve ter exatamente 2 caracteres

**Telefone:**
- `@NotBlank` - DDD e número são obrigatórios
- `@Size(min=2, max=3)` - DDD deve ter 2 ou 3 dígitos
- `@Size(min=8, max=9)` - Número deve ter 8 ou 9 dígitos
- `@Pattern(regexp="\\d+")` - DDD deve conter apenas números

## 🚀 Como Executar o Projeto

### Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

1. **Java JDK 17 ou superior**
   - Verifique a instalação executando no terminal:
     ```powershell
     java -version
     ```

2. **Maven** (Opcional - o projeto inclui Maven Wrapper)
   - O projeto possui o Maven Wrapper (`mvnw.cmd`), portanto não é necessário instalar o Maven separadamente

### Passos para Executar

#### 1️⃣ Clone ou Baixe o Projeto

```powershell
git clone https://github.com/KwMajor/autoBotsATVii
cd automanager
```

Ou navegue até a pasta do projeto se já tiver baixado:

```powershell
cd c:\Desktop\Autobots\automanager
```

#### 2️⃣ Compile o Projeto (Opcional, mas Recomendado)

Execute o comando abaixo para compilar e verificar se tudo está correto:

```powershell
.\mvnw.cmd clean compile
```

**Saída esperada:** `BUILD SUCCESS`

#### 3️⃣ Execute a Aplicação

**Opção A - Usando Maven Wrapper (Recomendado):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Opção B - Se você tiver Maven instalado globalmente:**

```powershell
mvn spring-boot:run
```

**Opção C - Executando o JAR compilado:**

```powershell
# Primeiro, compile e empacote
.\mvnw.cmd clean package

# Depois execute o JAR
java -jar target\automanager-0.0.1-SNAPSHOT.jar
```

#### 4️⃣ Verifique se a Aplicação Está Rodando

Quando a aplicação iniciar com sucesso, você verá mensagens como:

```
Tomcat started on port(s): 8080 (http) with context path ''
Started AutomanagerApplication in X.XXX seconds
```

### 🌐 Acessando a Aplicação

A aplicação estará disponível em: **http://localhost:8080**

#### 🏠 Root API (HATEOAS)

Acesse a raiz da API para visualizar todos os recursos disponíveis:

```
http://localhost:8080/
```

Retorno:
```json
{
  "message": "AutoBots API - Sistema de Gestão Veicular",
  "description": "API RESTful para gestão de veículos",
  "_links": {
    "clientes": {
      "href": "http://localhost:8080/cliente"
    },
    "enderecos": {
      "href": "http://localhost:8080/endereco"
    },
    "documentos": {
      "href": "http://localhost:8080/documento"
    },
    "telefones": {
      "href": "http://localhost:8080/telefone"
    },
    "self": {
      "href": "http://localhost:8080/"
    }
  }
}
```

#### 📚 Swagger UI (Documentação Interativa da API)

Acesse a interface do Swagger para **testar todos os endpoints interativamente**:


```
http://localhost:8080/swagger
```

#### H2 Console (Banco de Dados)

Para acessar o console do banco de dados H2:

```
http://localhost:8080/h2-console
```

**Configurações de conexão:**
- **JDBC URL:** `jdbc:h2:mem:autobots`
- **Username:** `sa`
- **Password:** *(deixe em branco)*

#### Endpoints da API

A API REST possui os seguintes recursos principais com **HATEOAS** e **validações**:

**Clientes:**
- `GET /cliente` - Lista todos os clientes (retorna CollectionModel com links)
- `GET /cliente/{id}` - Busca cliente por ID (retorna EntityModel com links)
- `POST /cliente` - Cria novo cliente (validação automática, retorna EntityModel)
- `PUT /cliente/{id}` - Atualiza cliente existente (validação automática)
- `DELETE /cliente/{id}` - Remove cliente

**Documentos:**
- `GET /documento` - Lista todos os documentos (retorna CollectionModel com links)
- `GET /documento/{id}` - Busca documento por ID (retorna EntityModel com links)
- `POST /documento` - Cria novo documento (validação automática, retorna EntityModel)
- `PUT /documento/{id}` - Atualiza documento existente (validação automática)
- `DELETE /documento/{id}` - Remove documento

**Endereços:**
- `GET /endereco` - Lista todos os endereços (retorna CollectionModel com links)
- `GET /endereco/{id}` - Busca endereço por ID (retorna EntityModel com links)
- `POST /endereco` - Cria novo endereço (validação automática, retorna EntityModel)
- `PUT /endereco/{id}` - Atualiza endereço existente (validação automática)
- `DELETE /endereco/{id}` - Remove endereço

**Telefones:**
- `GET /telefone` - Lista todos os telefones (retorna CollectionModel com links)
- `GET /telefone/{id}` - Busca telefone por ID (retorna EntityModel com links)
- `POST /telefone` - Cria novo telefone (validação automática, retorna EntityModel)
- `PUT /telefone/{id}` - Atualiza telefone existente (validação automática)
- `DELETE /telefone/{id}` - Remove telefone

#### 📊 Códigos de Status HTTP

A API retorna os seguintes códigos de status:

- **200 OK** - Requisição bem-sucedida (GET)
- **201 Created** - Recurso criado com sucesso (POST)
- **204 No Content** - Atualização/exclusão bem-sucedida (PUT/DELETE)
- **400 Bad Request** - Dados inválidos (validação falhou)
- **404 Not Found** - Recurso não encontrado

#### Actuator (Monitoramento)

Endpoints de monitoramento da aplicação:

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

### 🛑 Parar a Aplicação

Para parar a aplicação, pressione **Ctrl + C** no terminal onde ela está executando.

## 🧪 Testando a API

### Exemplo de Requisição POST - Criar Cliente

```json
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "João Silva",
  "nomeSocial": "João",
  "dataNascimento": "1990-05-15T00:00:00",
  "dataCadastro": "2025-11-20T00:00:00",
  "endereco": {
    "rua": "Rua das Flores",
    "numero": "123",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "codigoPostal": "01234-567"
  },
  "documentos": [
    {
      "tipo": "CPF",
      "numero": "123.456.789-00"
    }
  ],
  "telefones": [
    {
      "ddd": "11",
      "numero": "987654321"
    }
  ]
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "nomeSocial": "João",
  "dataNascimento": "1990-05-15T00:00:00.000+00:00",
  "dataCadastro": "2025-11-20T00:00:00.000+00:00",
  "endereco": {
    "id": 1,
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro",
    "rua": "Rua das Flores",
    "numero": "123",
    "codigoPostal": "01234-567",
    "informacoesAdicionais": null
  },
  "documentos": [
    {
      "id": 1,
      "tipo": "CPF",
      "numero": "123.456.789-00"
    }
  ],
  "telefones": [
    {
      "id": 1,
      "ddd": "11",
      "numero": "987654321"
    }
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/cliente/1"
    },
    "clientes": {
      "href": "http://localhost:8080/cliente"
    }
  }
}
```

### Exemplo de Requisição GET - Listar Clientes

```
GET http://localhost:8080/cliente
```

**Resposta (200 OK):**
```json
{
  "_embedded": {
    "clienteList": [
      {
        "id": 1,
        "nome": "João Silva",
        "_links": {
          "self": {
            "href": "http://localhost:8080/cliente/1"
          },
          "clientes": {
            "href": "http://localhost:8080/cliente"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/cliente"
    }
  }
}
```

### Exemplo de Validação - Dados Inválidos

```json
POST http://localhost:8080/cliente
Content-Type: application/json

{
  "nome": "Jo",
  "telefones": [
    {
      "ddd": "1",
      "numero": "123"
    }
  ]
}
```

**Resposta (400 Bad Request):**
```json
{
  "timestamp": "2025-11-20T18:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "Nome deve ter entre 3 e 100 caracteres",
    "DDD deve ter 2 ou 3 dígitos",
    "Número deve ter 8 ou 9 dígitos"
  ]
}
```

### Exemplo de Requisição GET - Buscar Cliente por ID

```
GET http://localhost:8080/cliente/1
```

**Resposta (200 OK):** Cliente completo com links HATEOAS

### Exemplo de Requisição PUT - Atualizar Cliente

```json
PUT http://localhost:8080/cliente/1
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "nomeSocial": "João Santos"
}
```

**Resposta (204 No Content):** Sem corpo, atualização bem-sucedida

### Exemplo de Requisição DELETE - Remover Cliente

```
DELETE http://localhost:8080/cliente/1
```

**Resposta (204 No Content):** Sem corpo, exclusão bem-sucedida

## ⚙️ Configurações

As configurações da aplicação estão no arquivo `src/main/resources/application.properties`:

```properties
# Configuração do H2
spring.datasource.url=jdbc:h2:mem:autobots
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Swagger/OpenAPI
springdoc.swagger-ui.path=/swagger
springdoc.api-docs.path=/v3/api-docs
```

## 📝 Troubleshooting

### Erro: "Port 8080 already in use"

Se a porta 8080 já estiver em uso, você pode:

1. Parar o processo que está usando a porta 8080
2. Ou alterar a porta no `application.properties`:
   ```properties
   server.port=8081
   ```

### Erro: "JAVA_HOME not set"

Configure a variável de ambiente JAVA_HOME:

```powershell
# No PowerShell (temporário)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Ou configure permanentemente nas variáveis de ambiente do sistema
```

### Erro ao executar mvnw.cmd

Se houver erro ao executar `mvnw.cmd`, tente:

```powershell
# Garanta que está no diretório correto
cd c:\Users\mates\OneDrive\Desktop\Autobots\automanager

# Use Push-Location para garantir o contexto correto
Push-Location "c:\Autobots\automanager"
.\mvnw.cmd spring-boot:run
```

## 📚 Estrutura do Banco de Dados

O Hibernate cria automaticamente as seguintes tabelas:

- `cliente` - Dados principais dos clientes
  - Campos: id, nome, nome_social, data_nascimento, data_cadastro, endereco_id
  
- `documento` - Documentos dos clientes
  - Campos: id, tipo, numero (unique)
  
- `endereco` - Endereços dos clientes
  - Campos: id, estado, cidade, bairro, rua, numero, codigo_postal, informacoes_adicionais
  
- `telefone` - Telefones dos clientes
  - Campos: id, ddd, numero
  
- `cliente_documentos` - Tabela de relacionamento Cliente-Documento (N:N)
  - Campos: cliente_id (FK), documentos_id (FK, unique)
  
- `cliente_telefones` - Tabela de relacionamento Cliente-Telefone (N:N)
  - Campos: cliente_id (FK), telefones_id (FK, unique)

### Diagrama de Relacionamentos

```
┌─────────────┐         ┌──────────────┐
│   Cliente   │───1:N───│  Documento   │
│             │         │              │
│ - id        │         │ - id         │
│ - nome      │         │ - tipo       │
│ - nomeSocial│         │ - numero     │
│             │         └──────────────┘
│             │
│             │───1:1───┌──────────────┐
│             │         │   Endereco   │
│             │         │              │
│             │         │ - id         │
│             │         │ - cidade     │
│             │         │ - rua        │
│             │         └──────────────┘
│             │
│             │───1:N───┌──────────────┐
│             │         │   Telefone   │
└─────────────┘         │              │
                        │ - id         │
                        │ - ddd        │
                        │ - numero     │
                        └──────────────┘
```

## 🏗️ Padrões de Projeto Implementados

### 1. **Repository Pattern**
Abstração da camada de persistência usando Spring Data JPA:
- `ClienteRepository`
- `DocumentoRepository`
- `EnderecoRepository`
- `TelefoneRepository`

### 2. **Selecionador Pattern (Helper)**
Classes auxiliares para seleção de entidades por ID:
- `ClienteSelect`
- `DocumentoSelect`
- `EnderecoSelect`
- `TelefoneSelect`

### 3. **Atualizador Pattern (Updater)**
Classes para atualização parcial de entidades:
- `ClienteAtualizador`
- `DocumentoAtualizador`
- `EnderecoAtualizador`
- `TelefoneAtualizador`

### 4. **RESTful API com HATEOAS**
Implementação completa de HATEOAS (Hypermedia as the Engine of Application State):
- `EntityModel<T>` para recursos individuais
- `CollectionModel<EntityModel<T>>` para coleções
- Links de navegação automáticos (self, collection)

### 5. **Bean Validation (JSR-303)**
Validação declarativa com anotações:
- `@NotBlank`, `@NotNull`
- `@Size`, `@Pattern`
- `@Past` (datas no passado)
- `@Valid` para validação em cascata

## 👨‍💻 Desenvolvimento

### Executar em Modo de Desenvolvimento

A aplicação já vem configurada com Spring Boot DevTools, que permite:

- Reinicialização automática ao alterar arquivos
- LiveReload para recarregar páginas automaticamente

### Estrutura de Pastas

```
automanager/
├── src/
│   ├── main/
│   │   ├── java/com/autobots/automanager/
│   │   │   ├── AutomanagerApplication.java (Classe principal)
│   │   │   ├── controles/         (Controllers REST com HATEOAS)
│   │   │   │   ├── ClienteController.java
│   │   │   │   ├── DocumentoController.java
│   │   │   │   ├── EnderecoController.java
│   │   │   │   ├── TelefoneController.java
│   │   │   │   └── RootControle.java (Root API com links)
│   │   │   ├── entidades/         (Entidades JPA com validações)
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Documento.java
│   │   │   │   ├── Endereco.java
│   │   │   │   └── Telefone.java
│   │   │   ├── repositorios/      (Repositories Spring Data JPA)
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── DocumentoRepository.java
│   │   │   │   ├── EnderecoRepository.java
│   │   │   │   └── TelefoneRepository.java
│   │   │   └── modelo/            (Classes auxiliares)
│   │   │       ├── ClienteSelect.java
│   │   │       ├── ClienteAtualizador.java
│   │   │       ├── DocumentoSelect.java
│   │   │       ├── DocumentoAtualizador.java
│   │   │       ├── EnderecoSelect.java
│   │   │       ├── EnderecoAtualizador.java
│   │   │       ├── TelefoneSelect.java
│   │   │       ├── TelefoneAtualizador.java
│   │   │       └── StringVerificadorNulo.java
│   │   └── resources/
│   │       └── application.properties (Configurações)
│   └── test/                       (Testes unitários)
├── target/                         (Arquivos compilados)
├── mvnw, mvnw.cmd                  (Maven Wrapper)
├── pom.xml                         (Configuração Maven)
└── README.md                       (Este arquivo)
```

## 📖 Documentação da API

### Swagger UI
A documentação completa da API está disponível através do Swagger UI em:
- **URL:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

### Características da Documentação:
- ✅ Todos os endpoints documentados com `@Operation`
- ✅ Códigos de status HTTP documentados com `@ApiResponse`
- ✅ Tags organizando recursos com `@Tag`
- ✅ Exemplos de requisições e respostas
- ✅ Modelos de dados com validações visíveis

## 🎯 Requisitos Atendidos (ATVI II)

- ✅ **CRUD Completo** - Create, Read, Update, Delete para todos os recursos
- ✅ **HATEOAS** - Hypermedia links em todos os endpoints
- ✅ **Spring Data JPA** - Repositories e relacionamentos JPA
- ✅ **Bean Validation** - Validação automática com JSR-303
- ✅ **Swagger/OpenAPI** - Documentação completa e interativa
- ✅ **H2 Database** - Banco em memória com console web
- ✅ **Padrão Repository** - Abstração da camada de persistência
- ✅ **Padrão Selecionador** - Classes helper para busca por ID
- ✅ **Padrão Atualizador** - Atualização parcial de entidades
- ✅ **REST Compliant** - Códigos HTTP corretos, verbos adequados
- ✅ **Lombok** - Redução de boilerplate code
- ✅ **Spring Boot DevTools** - Hot reload para desenvolvimento

## 📄 Licença

Projeto desenvolvido para fins educacionais.

---

**Desenvolvido com ❤️ usando Spring Boot**
