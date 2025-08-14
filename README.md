# Sistema de Entrega Logística

Este projeto é um sistema completo para **agendamento de caminhões**, desenvolvido como parte de um teste técnico.  
Inclui **back-end** feito em **Spring Boot** (Java 17) e **front-end** feito em **Vue.js** com **TypeScript**.

---

## Estrutura do Projeto

/backend → API REST em Spring Boot
/frontend → Aplicação Vue.js


---

## Tecnologias Utilizadas

**Back-end:**
- Java 17
- Spring Boot (Security, Web, Lombok, JWT, Validation, SQLite)
- JPA / Hibernate
- SQLite (banco local, gerado automaticamente)
- JWT para autenticação
- Perfis de usuário (ADMIN e USER)

**Front-end:**
- Vue 3
- TypeScript
- Pinia (gerenciamento de estado)
- Vue Router
- Axios (requisições HTTP)

---

## Requisitos

- **Java 17** ou superior
- **Maven**
- **Node.js** (versão 22+ recomendada)
- Portas **8080** (back-end) e **5173** (front-end) devem estar livres

---

## Banco de Dados

O projeto usa **SQLite** como banco local por facilidade de instalação.  
O arquivo `.db` é criado automaticamente quando o back-end é inicializado pela primeira vez.

Em um ambiente de produção de grande escala, recomenda-se utilizar **MySQL** hospedado em um servidor dedicado.

---

## Usuários Padrão

Ao iniciar o back-end pela primeira vez, são criados automaticamente dois usuários para teste:

- **Administrador**
  - E-mail: `admin@krill.local`
  - Senha: `admin123`
  - Permissões: Cadastrar, visualizar e cancelar agendamentos.

- **Usuário Comum**
  - E-mail: `user@krill.local`
  - Senha: `user123`
  - Permissões: Apenas visualizar agendamentos.

---

## Configuração do Front-end

No diretório `/frontend`, crie um arquivo `.env` com o seguinte conteúdo:

```env
API_URL=http://localhost:8080/api/v1

Esse valor será usado para que o front-end se comunique com a API.
Passos para Instalação e Execução
1. Back-end

cd backend
mvn clean install
mvn spring-boot:run

O back-end estará disponível em:

localhost:8080/api/v1

2. Front-end

cd frontend
npm install
npm run dev

O front-end estará disponível em:

http://localhost:5173

Fluxo de Autenticação

    Acesse a tela de login no front-end.

    Faça login com um dos usuários de teste.

    O token JWT retornado será usado automaticamente pelo front-end para autenticar as requisições.

    Usuários ADMIN têm acesso total, usuários USER apenas visualizam agendamentos.

Observações

    Certifique-se que as portas 8080 e 5173 estejam livres antes de iniciar.

    O arquivo node_modules e target não são versionados (veja .gitignore).

    Para ambiente de produção, configurar variáveis de ambiente seguras (JWT Secret, URL do banco de dados, etc.).
