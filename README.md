# Clínica HM - Backend

Este repositório contém o código-fonte do backend da aplicação Clínica HM, desenvolvida com Java e Spring Boot.

---

📋 **Índice**

* [🔍 Visão Geral](#-visão-geral)
* [🏗️ Arquitetura](#️-arquitetura)
* [🛠️ Tecnologias](#️-tecnologias)
* [✨ Funcionalidades (Visão Backend)](#-funcionalidades-visão-backend)
* [⚙️ Configuração](#️-configuração)
* [📂 Estrutura do Projeto](#-estrutura-do-projeto)
* [📄 Licença](#-licença)

---

## 🔍 Visão Geral

O backend da Clínica HM é o responsável pela lógica de negócio, persistência de dados e por fornecer a API RESTful consumida pelo frontend. Ele gerencia as informações de pacientes, médicos e prontuários de forma segura e transacional.

## 🏗️ Arquitetura

O backend segue uma arquitetura em camadas para promover a separação de responsabilidades (SoC).

src/main/java/com/main/
├── api/
│   ├── controller/      # Controladores REST
│   ├── exception/       # Classes de exceção personalizadas
│   └── model/           # DTOs (Data Transfer Objects) para requisições e respostas
├── config/              # Configurações do Spring (Security, ModelMapper)
├── domain/
│   ├── entity/          # Entidades JPA (modelos de dados)
│   ├── repository/      # Interfaces de repositório Spring Data JPA
│   └── service/         # Lógica de negócio principal
├── mapper/              # Classes para mapeamento entre DTOs e entidades
└── util/                # Classes utilitárias (JWT, ApiResponse)


## 🛠️ Tecnologias

* **Java 17**: Linguagem de programação.
* **Spring Boot 3.1.2**: Framework para desenvolvimento de aplicações Java.
* **Spring Security**: Segurança e autenticação, com suporte a JWT.
* **Spring Data JPA**: Abstração para persistência de dados.
* **ModelMapper 3.1.1**: Facilita o mapeamento entre objetos.
* **JJWT (Java JWT) 0.11.5**: Implementação de JSON Web Tokens.
* **MySQL 9.0.0 (Connector)**: Banco de dados relacional.
* **Maven**: Ferramenta de gerenciamento de dependências.

## ✨ Funcionalidades (Visão Backend)

* **Autenticação e Autorização**: Gerenciamento de login, registro, recuperação de senha e controle de acesso baseado em JWT para administradores.
* **Gestão de Pacientes**: APIs para operações CRUD (Criar, Ler, Atualizar, Deletar) de pacientes, incluindo validações de CPF/email e gerenciamento de coleções como alergias e comorbidades.
* **Gestão de Médicos**: APIs para operações CRUD de médicos, com validação de CRM e funcionalidade de ativação/inativação.
* **Gerenciamento de Prontuários**:
    * Criação de prontuários (se não existirem) ao adicionar o primeiro registro (consulta, exame, procedimento, encaminhamento).
    * APIs para adicionar e atualizar diferentes tipos de registros ao prontuário, como consultas, exames, procedimentos e encaminhamentos.
    * Busca detalhada de prontuários, incluindo todos os seus registros associados.
    * Atualização do médico responsável principal do prontuário.

📂 Estrutura do Projeto
.
├── pom.xml                        # Gerenciamento de dependências Maven
├── src/
│   ├── main/
│   │   ├── java/com/main/
│   │   │   ├── api/               # Controladores, exceções e DTOs
│   │   │   ├── config/            # Configurações do Spring
│   │   │   ├── domain/            # Entidades, repositórios e serviços de domínio
│   │   │   ├── mapper/            # Mapeadores de objetos
│   │   │   └── util/              # Classes utilitárias (JWT, ApiResponse)
│   │   └── resources/             # Arquivos de configuração (application.properties)
│   └── test/                      # Testes unitários e de integração
└── .gitignore                     # Arquivos a serem ignorados pelo Git

📄 Licença
Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE.md na raiz do projeto principal para detalhes.
