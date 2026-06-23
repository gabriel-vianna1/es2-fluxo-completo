# Saúde Web

Sistema de gestão de profissionais de saúde e atendimentos.

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17 + Spring Boot 3.2 |
| Frontend | React 18 + React Router |
| Banco de Dados | PostgreSQL 15 |
| Build Backend | Maven |
| Build Frontend | Node.js 20 + npm |
| Versionamento | Git + GitHub |
| CI/CD | GitHub Actions |
| Containers | Docker + Docker Compose |
| Produção | Render (Blueprint render.yaml) |

## Entidades

- **ProfissionalSaude** — nome, telefone, endereço, categoria (Médico / Fisioterapeuta / Psicólogo)
- **Atendimento** — título, data, horário, link de videoconferência, tipo de receita → FK ProfissionalSaude
- **ExameLaboratorio** — descrição, posologia → FK Atendimento

## Estrutura do Projeto

```
saude-web/
├── backend/           # API REST (Java/Spring Boot)
├── frontend/          # UI (React 18)
├── docker-compose.yml
├── render.yaml        # Deploy automático no Render
└── .github/workflows/ci-cd.yml
```

## Como Executar

```bash
docker-compose up -d
# Backend: http://localhost:8080
# Frontend: http://localhost:3000
# Swagger: http://localhost:8080/swagger-ui.html
```

## Endpoints da API

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /api/profissionais | Listar todos |
| GET | /api/profissionais?nome=xxx | Buscar por nome |
| GET | /api/profissionais/categoria/{cat} | Buscar por categoria |
| POST | /api/profissionais | Inserir |
| PUT | /api/profissionais/{id} | Alterar |
| DELETE | /api/profissionais/{id} | Excluir |
| GET | /api/atendimentos | Listar todos |
| POST | /api/atendimentos | Inserir |
| PUT | /api/atendimentos/{id} | Alterar |
| DELETE | /api/atendimentos/{id} | Excluir |
| GET | /api/exames | Listar todos |
| POST | /api/exames | Inserir |
| PUT | /api/exames/{id} | Alterar |
| DELETE | /api/exames/{id} | Excluir |
