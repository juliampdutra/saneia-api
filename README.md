# Saneia API

API REST do **Saneia** — Global Solution 2026 (FIAP, turma 3SIR) — disciplina *Microservice and Web Engineering & IT Services*.

| Integrante | RM |
|---|---|
| Julia Mendes Pessoa Dutra | RM557645 |
| Rebeca Berbert Barcelos | RM556683 |

| Recurso | Link |
|---|---|
| API no Azure (Swagger) | **https://URL-DO-AZURE** |
| Docker Hub | https://hub.docker.com/r/juliampdutra/saneia-api |
| GitHub Actions | https://github.com/juliampdutra/saneia-api/actions |

---

## 1. Objetivo da solução

Moradores de São Paulo convivem com problemas de saneamento — esgoto a céu aberto, vazamentos, falta d'água e bueiros entupidos — e muitas vezes não têm um canal simples para reportá-los nem para acompanhar a resolução.

O **Saneia** permite que o morador **registre ocorrências de saneamento por distrito** e que as equipes responsáveis **agendem e registrem atendimentos** até a ocorrência ser resolvida. Com isso, fica possível enxergar onde os problemas se concentram e acompanhar o ciclo completo: *aberta → em atendimento → resolvida*.

Esta API é o microsserviço de backend da solução (o app mobile Flutter consome os dados).

---

## 2. Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.9 (Web MVC, Data JPA, Validation) |
| Microsoft SQL Server | Azure SQL Database (nuvem) / SQL Server 2022 em Docker (local) |
| springdoc-openapi (Swagger UI) | 3.0.2 |
| ModelMapper | 3.1.1 |
| Docker | build multi-stage (Maven + JRE Alpine) |
| GitHub Actions | build e publicação automática no Docker Hub |
| Azure | Azure Container Apps + Azure SQL Database |

---

## 3. Arquitetura

```
Cliente (Swagger / Postman / App)
        │  JSON (DTOs)
        ▼
Controller  ──►  Service  ──►  Repository  ──►  SQL Server
 (rotas v1)     (regras de     (Spring Data
                 negócio +      JPA)
                 ModelMapper)
```

- **Controller** recebe e devolve apenas **DTOs** e só conhece a **Service** (nunca acessa Repository).
- **Service** concentra as **regras de negócio**, converte DTO ⇄ Entity com **ModelMapper** e chama o Repository.
- **Repository** (`JpaRepository`) faz a persistência no SQL Server.
- **Entities** nunca são expostas nas requisições/respostas.
- **GlobalExceptionHandler** (`@RestControllerAdvice`) padroniza todos os erros.

```
src/main/java/br/com/fiap/saneia
├── config       ModelMapperConfig, OpenApiConfig, DataHora
├── controller   DistritoController, MoradorController, OcorrenciaController, AtendimentoController
├── dto
│   ├── request  *Request  (entrada + Bean Validation)
│   └── response *Response (saída)
├── exception    GlobalExceptionHandler, ErroResponse, CampoInvalido,
│                RecursoNaoEncontradoException, RegraNegocioException
├── model        Distrito, Morador, Ocorrencia, Atendimento (@Entity)
│   └── enums    Zona, TipoOcorrencia, Prioridade, StatusOcorrencia, StatusAtendimento
├── repository   *Repository (JpaRepository)
└── service      *Service (regras de negócio)
```

---

## 4. Modelo de dados

```
DISTRITOS 1 ───< N MORADORES
    │                 │
    │ 1               │ 1
    └────< N OCORRENCIAS >────┘
                 │ 1
                 └────< N ATENDIMENTOS
```

| Entidade | Tabela | Atributos (* obrigatório) | Relacionamento |
|---|---|---|---|
| Distrito | `distritos` | nome* (único), zona*, subprefeitura*, codigoIbge, populacaoEstimada, areaKm2 | — |
| Morador | `moradores` | nome*, email* (único), telefone*, cep, logradouro, dataCadastro* | N:1 Distrito |
| Ocorrencia | `ocorrencias` | titulo*, descricao*, tipo*, prioridade*, enderecoReferencia*, status*, dataAbertura*, dataResolucao | N:1 Morador, N:1 Distrito |
| Atendimento | `atendimentos` | equipeResponsavel*, dataAgendada*, descricaoServico, status*, custoEstimado, dataConclusao | N:1 Ocorrencia |

Enums: `Zona` (NORTE, SUL, LESTE, OESTE, CENTRO) · `TipoOcorrencia` (ESGOTO_A_CEU_ABERTO, VAZAMENTO, FALTA_DE_AGUA, BUEIRO_ENTUPIDO, OUTRO) · `Prioridade` (BAIXA, MEDIA, ALTA, URGENTE) · `StatusOcorrencia` (ABERTA, EM_ATENDIMENTO, RESOLVIDA, CANCELADA) · `StatusAtendimento` (AGENDADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO).

As tabelas são criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

### Regras de negócio (camada Service)

- `dataCadastro` (morador) e `dataAbertura` (ocorrência) são definidas pela API e preservadas no PUT.
- Toda ocorrência nasce **ABERTA**; `dataResolucao` só é preenchida quando ela fica **RESOLVIDA**.
- E-mail do morador é salvo em minúsculas (evita duplicidade por maiúsculas/minúsculas).
- IDs relacionados (`distritoId`, `moradorId`, `ocorrenciaId`) inexistentes → **404**.
- Não é possível agendar atendimento para ocorrência **RESOLVIDA** ou **CANCELADA** → **422**.
- Atendimento **AGENDADO/EM_ANDAMENTO** → a ocorrência passa para **EM_ATENDIMENTO**.
- Atendimento **CONCLUIDO** → registra `dataConclusao` e a ocorrência passa para **RESOLVIDA**.

---

## 5. Endpoints (versão v1)

A rota raiz **`/`** redireciona para o **Swagger UI**. Documentação OpenAPI em JSON: `/v3/api-docs`.

| Recurso | POST | GET (listar) | GET /{id} | PUT /{id} | DELETE /{id} |
|---|---|---|---|---|---|
| Distritos | `/api/v1/distritos` | `/api/v1/distritos` | `/api/v1/distritos/{id}` | `/api/v1/distritos/{id}` | `/api/v1/distritos/{id}` |
| Moradores | `/api/v1/moradores` | `/api/v1/moradores` | `/api/v1/moradores/{id}` | `/api/v1/moradores/{id}` | `/api/v1/moradores/{id}` |
| Ocorrências | `/api/v1/ocorrencias` | `/api/v1/ocorrencias` | `/api/v1/ocorrencias/{id}` | `/api/v1/ocorrencias/{id}` | `/api/v1/ocorrencias/{id}` |
| Atendimentos | `/api/v1/atendimentos` | `/api/v1/atendimentos` | `/api/v1/atendimentos/{id}` | `/api/v1/atendimentos/{id}` | `/api/v1/atendimentos/{id}` |

### Códigos HTTP

| Código | Quando |
|---|---|
| **201 Created** | POST com sucesso (corpo com o registro criado + header `Location`) |
| **200 OK** | GET (listar/buscar) e PUT com sucesso |
| **204 No Content** | DELETE com sucesso (sem corpo) |
| **400 Bad Request** | Validação falhou, JSON malformado, enum inexistente ou ID em formato inválido (`/distritos/abc`) |
| **404 Not Found** | ID inexistente (inclusive em PUT e DELETE), ID relacionado inexistente ou rota inexistente |
| **409 Conflict** | Violação de integridade: nome de distrito/e-mail duplicado, ou exclusão de registro que possui vínculos |
| **422 Unprocessable Entity** | Regra de negócio: atendimento para ocorrência já RESOLVIDA/CANCELADA (a requisição é válida no formato, mas não pode ser processada no estado atual do recurso) |
| **500 Internal Server Error** | Falha inesperada — mensagem neutra, sem stack trace nem detalhes internos |

### Corpo padrão de erro

```json
{
  "timestamp": "2026-11-20T10:15:30",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos. Verifique os campos informados.",
  "path": "/api/v1/moradores",
  "campos": [
    { "campo": "email", "mensagem": "E-mail inválido" }
  ]
}
```

---

## 6. Como executar localmente

### 6.1 Pré-requisitos

- Java 21 (JDK) · Docker Desktop · Git
- Maven **não** precisa ser instalado (o projeto usa o Maven Wrapper `mvnw`)

### 6.2 Subir o SQL Server em Docker

Escolha uma senha forte (mínimo 8 caracteres com maiúscula, minúscula, número e símbolo) e substitua `<SUA_SENHA>`:

```bash
docker run -d --name saneia-sqlserver \
  -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=<SUA_SENHA>" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2022-latest
```

Aguarde ~20 segundos e crie o banco `saneia`:

```bash
docker exec -it saneia-sqlserver /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P "<SUA_SENHA>" -C \
  -Q "CREATE DATABASE saneia"
```

### 6.3 Configurar a conexão (sem expor senha)

O `application.properties` lê a conexão de variáveis de ambiente (`DB_URL`, `DB_USER`, `DB_PASSWORD`). Para rodar local, copie o modelo e preencha a senha:

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

> `application-local.properties` está no `.gitignore` e no `.dockerignore`: nunca vai para o GitHub nem para a imagem.

### 6.4 Rodar o Spring Boot

Windows (PowerShell):
```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

Linux/macOS:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Acesse **http://localhost:8080/** (redireciona para o Swagger UI).

Alternativa sem perfil local — variáveis de ambiente:
```bash
export DB_URL="jdbc:sqlserver://localhost:1433;databaseName=saneia;encrypt=true;trustServerCertificate=true"
export DB_USER="sa"
export DB_PASSWORD="<SUA_SENHA>"
./mvnw spring-boot:run
```

### 6.5 Gerar o .jar

```bash
./mvnw clean package -DskipTests   # gera target/app.jar
```

> Os testes são pulados no empacotamento porque o teste de contexto precisa de um banco disponível.

---

## 7. Docker

### 7.1 Imagem publicada no Docker Hub

| | |
|---|---|
| Repositório | https://hub.docker.com/r/juliampdutra/saneia-api |
| Imagem | `juliampdutra/saneia-api` |
| Tags | `latest` (última versão da `main`) e `<SHA do commit>` (versão exata de cada build) |
| Plataforma | `linux/amd64` |

```bash
docker pull juliampdutra/saneia-api:latest
```

### 7.2 Executar a imagem

Com o SQL Server local em Docker (do passo 6.2), use `host.docker.internal` para o container da API alcançar o banco no host:

```bash
docker run -d --name saneia-api -p 8080:8080 \
  -e DB_URL="jdbc:sqlserver://host.docker.internal:1433;databaseName=saneia;encrypt=true;trustServerCertificate=true" \
  -e DB_USER="sa" \
  -e DB_PASSWORD="<SUA_SENHA>" \
  juliampdutra/saneia-api:latest
```

Acesse http://localhost:8080/

### 7.3 Construir a imagem localmente

```bash
docker build -t juliampdutra/saneia-api:local .
```

O `Dockerfile` é **multi-stage**:
1. **build** — `maven:3.9-eclipse-temurin-21` compila e gera `target/app.jar`;
2. **runtime** — `eclipse-temurin:21-jre-alpine` contém só o JRE e o `.jar`, executando com usuário sem privilégios de root.

Nenhuma credencial é copiada para a imagem: a conexão é passada em tempo de execução via variáveis de ambiente.

---

## 8. Continuous Delivery — GitHub Actions

Workflow: [`.github/workflows/docker-publish.yml`](.github/workflows/docker-publish.yml)

| Etapa | O que faz |
|---|---|
| Gatilho | Executa **automaticamente a cada `push` na branch `main`** (também pode ser disparado manualmente) |
| Checkout + Java 21 | Baixa o código e configura o JDK Temurin 21 com cache do Maven |
| Maven | `mvn -B clean package -DskipTests` — compila e empacota a aplicação |
| Login | Autentica no Docker Hub usando os secrets `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` |
| Build & Push | Constrói a imagem pelo `Dockerfile` (plataforma `linux/amd64`) e publica com as tags `latest` e `<SHA do commit>` |

Secrets configurados em *Settings → Secrets and variables → Actions* (nenhum valor fica no código):

| Secret | Conteúdo |
|---|---|
| `DOCKERHUB_USERNAME` | usuário do Docker Hub |
| `DOCKERHUB_TOKEN` | Personal Access Token do Docker Hub (Read & Write) |

Histórico de execuções: https://github.com/juliampdutra/saneia-api/actions

---

## 9. Implantação no Azure

| Recurso | Serviço |
|---|---|
| Aplicação | **Azure Container Apps** — container criado a partir da imagem `juliampdutra/saneia-api:latest` do Docker Hub, ingress externo na porta 8080 |
| Banco | **Azure SQL Database** (Microsoft SQL Server) — banco `saneia`, conexão criptografada (`encrypt=true`) |

**URL da API:** https://URL-DO-AZURE

A aplicação recebe a conexão por variáveis de ambiente configuradas no Container App (`DB_URL`, `DB_USER` e `DB_PASSWORD` — esta última como *secret*). Formato da `DB_URL` (sem credenciais):

```
jdbc:sqlserver://<servidor>.database.windows.net:1433;database=saneia;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
```

---

## 10. Como testar

Abra o Swagger (local: http://localhost:8080/ · Azure: https://URL-DO-AZURE/) e execute **na ordem abaixo** (cada recurso depende do anterior). Os exemplos também já aparecem preenchidos no *Try it out* do Swagger.

### 10.1 Fluxo de sucesso

**1) POST `/api/v1/distritos` → 201**
```json
{
  "nome": "Sé",
  "zona": "CENTRO",
  "subprefeitura": "Sé",
  "codigoIbge": "355030877",
  "populacaoEstimada": 23651,
  "areaKm2": 2.1
}
```

**2) POST `/api/v1/moradores` → 201**
```json
{
  "nome": "Maria da Silva",
  "email": "maria.silva@email.com",
  "telefone": "11987654321",
  "cep": "01001-000",
  "logradouro": "Praça da Sé, 100",
  "distritoId": 1
}
```

**3) POST `/api/v1/ocorrencias` → 201** (nasce com `status: ABERTA`)
```json
{
  "titulo": "Vazamento na calçada",
  "descricao": "Água limpa escorrendo pela calçada há 2 dias",
  "tipo": "VAZAMENTO",
  "prioridade": "ALTA",
  "enderecoReferencia": "Rua Direita, 250 - em frente à padaria",
  "moradorId": 1,
  "distritoId": 1
}
```

**4) POST `/api/v1/atendimentos` → 201** (a ocorrência 1 passa para `EM_ATENDIMENTO`)
```json
{
  "equipeResponsavel": "Equipe Sabesp Centro 03",
  "dataAgendada": "2026-11-20T09:00:00",
  "descricaoServico": "Troca do trecho da tubulação danificada",
  "custoEstimado": 350.00,
  "ocorrenciaId": 1
}
```

**5) PUT `/api/v1/atendimentos/1` → 200** (concluir: a ocorrência 1 passa para `RESOLVIDA`)
```json
{
  "equipeResponsavel": "Equipe Sabesp Centro 03",
  "dataAgendada": "2026-11-20T09:00:00",
  "descricaoServico": "Tubulação substituída",
  "status": "CONCLUIDO",
  "custoEstimado": 380.00,
  "ocorrenciaId": 1
}
```

**6) GET `/api/v1/ocorrencias/1` → 200** — confirme `status: RESOLVIDA` e `dataResolucao` preenchida.

**7) GET `/api/v1/distritos`, `/moradores`, `/ocorrencias`, `/atendimentos` → 200** — os registros continuam lá (persistência no SQL Server).

### 10.2 Cenários de erro

| Teste | Requisição | Esperado |
|---|---|---|
| Campo obrigatório vazio | POST `/api/v1/moradores` com `{"nome": "", "email": "x", "telefone": "1", "distritoId": 1}` | **400** + lista `campos` |
| Enum inválido | POST `/api/v1/distritos` com `"zona": "NORDESTE"` | **400** |
| JSON malformado | POST `/api/v1/distritos` com `{ "nome": ` | **400** |
| ID em formato inválido | GET `/api/v1/distritos/abc` | **400** |
| ID inexistente | GET `/api/v1/distritos/999` | **404** |
| PUT em inexistente | PUT `/api/v1/moradores/999` (corpo válido) | **404** |
| DELETE em inexistente | DELETE `/api/v1/ocorrencias/999` | **404** |
| Relacionado inexistente | POST `/api/v1/moradores` com `"distritoId": 999` | **404** |
| E-mail duplicado | POST `/api/v1/moradores` repetindo `maria.silva@email.com` | **409** |
| Excluir com vínculos | DELETE `/api/v1/distritos/1` (possui moradores) | **409** |
| Regra de negócio | POST `/api/v1/atendimentos` para a ocorrência 1 (já RESOLVIDA) | **422** |
| Exclusão com sucesso | POST um distrito novo e DELETE `/api/v1/distritos/{id}` | **204** |

### 10.3 Exemplo com curl

```bash
curl -i -X POST https://URL-DO-AZURE/api/v1/distritos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Pinheiros","zona":"OESTE","subprefeitura":"Pinheiros"}'

curl -i https://URL-DO-AZURE/api/v1/distritos
```

---

## 11. Segurança das credenciais

- Nenhuma senha, token ou connection string com credenciais está no repositório.
- `application.properties` lê `DB_URL`, `DB_USER` e `DB_PASSWORD` de variáveis de ambiente.
- `application-local.properties` (uso local) está no `.gitignore` e no `.dockerignore`.
- Credenciais do Docker Hub ficam em **GitHub Secrets**; a senha do banco fica como **secret do Azure Container App**.
- Respostas de erro nunca expõem stack trace (`spring.web.error.include-stacktrace=never` + handler global com mensagem neutra no 500).
