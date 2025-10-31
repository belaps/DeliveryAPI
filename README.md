# 🍕 Delivery Tech API

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue?logo=apachemaven)
![License](https://img.shields.io/badge/license-MIT-green)

API REST completa para sistema de delivery de alimentos, desenvolvida com **Java 21** e **Spring Boot 3.5.7**, utilizando recursos modernos da linguagem.

## 📋 Sobre o Projeto

O **Delivery Tech API** é um sistema de delivery completo que permite:
- Gerenciamento de clientes
- Cadastro e busca de restaurantes por categoria
- Controle de produtos e cardápios
- Criação e acompanhamento de pedidos
- Atualização de status em tempo real

Este projeto foi desenvolvido como parte de um curso prático de **Spring Boot com Java 21**, demonstrando as melhores práticas e recursos modernos do ecossistema Java.

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21** (LTS) - Versão mais recente com recursos modernos
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência e ORM
- **Spring Validation** - Validação de dados
- **H2 Database** - Banco de dados em memória

### Recursos Modernos do Java 21
- ✅ **Records** - Classes imutáveis para DTOs
- ✅ **Pattern Matching** - Switch expressions modernas
- ✅ **Text Blocks** - Strings multilinha
- ✅ **Sealed Classes** - Hierarquia de tipos controlada
- ✅ **Virtual Threads** - Concorrência simplificada (potencial)

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Git** - Controle de versão
- **H2 Console** - Interface web para o banco

## 📦 Estrutura do Projeto

```
delivery-api/
├── src/
│   ├── main/
│   │   ├── java/com/deliverytech/delivery_api/
│   │   │   ├── controller/          # Controllers REST
│   │   │   │   ├── ClienteController.java
│   │   │   │   ├── RestauranteController.java
│   │   │   │   ├── ProdutoController.java
│   │   │   │   └── PedidoController.java
│   │   │   ├── service/              # Lógica de negócio
│   │   │   │   ├── ClienteService.java
│   │   │   │   ├── RestauranteService.java
│   │   │   │   ├── ProdutoService.java
│   │   │   │   └── PedidoService.java
│   │   │   ├── repository/           # Camada de dados
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── RestauranteRepository.java
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   └── PedidoRepository.java
│   │   │   ├── entity/               # Entidades JPA
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Restaurante.java
│   │   │   │   ├── Produto.java
│   │   │   │   └── Pedido.java
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   │   ├── ClienteDTO.java
│   │   │   │   ├── RestauranteDTO.java
│   │   │   │   ├── ProdutoDTO.java
│   │   │   │   └── PedidoDTO.java
│   │   │   ├── exception/            # Tratamento de exceções
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── DeliveryApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/deliverytech/delivery_api/
│           └── repository/           # Testes unitários
├── activity-files/                   # Screenshots e evidências
├── test-endpoints.sh                 # Script de testes (Linux/Mac)
├── test-endpoints.ps1                # Script de testes (Windows)
├── ATIVIDADES.md                     # Documentação das atividades
├── pom.xml
└── README.md
```

## ⚙️ Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** ou superior ([Download](https://adoptium.net/))
- **Maven 3.9+** (ou use o wrapper incluído `./mvnw`)
- **Git** (para clonar o repositório)

### Verificar Instalação

```bash
# Verificar versão do Java
java -version
# Deve mostrar: openjdk version "21.x.x"

# Verificar Maven
mvn -version
# Deve mostrar: Apache Maven 3.9.x

# Verificar Git
git --version
```

## 🔧 Instalação e Configuração

### 1. Clonar o Repositório

```bash
git clone https://github.com/belaps/DeliveryAPI.git
cd delivery-api
```

### 2. Compilar o Projeto

**Linux/Mac:**
```bash
./mvnw clean install
```

**Windows:**
```cmd
mvnw.cmd clean install
```

### 3. Executar a Aplicação

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 4. Acessar o Console H2

Durante o desenvolvimento, você pode acessar o console do banco H2:

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:deliverydb`
- **Username:** `sa`
- **Password:** *(deixe em branco)*

## 📚 Endpoints da API

### 🔹 Health Check

```bash
GET /health
```

Retorna o status da aplicação e versão do Java.

### 👤 Clientes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/clientes` | Criar novo cliente |
| GET | `/clientes` | Listar todos os clientes |
| GET | `/clientes?nome=X` | Buscar por nome |
| GET | `/clientes?ativo=true` | Buscar clientes ativos |
| GET | `/clientes/{id}` | Buscar por ID |
| PUT | `/clientes/{id}` | Atualizar cliente |
| DELETE | `/clientes/{id}` | Deletar cliente |

**Exemplo - Criar Cliente:**
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "11999999999",
    "endereco": "Rua A, 123",
    "ativo": true
  }'
```

### 🍽️ Restaurantes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/restaurantes` | Criar novo restaurante |
| GET | `/restaurantes` | Listar todos |
| GET | `/restaurantes?categoria=X` | Buscar por categoria |
| GET | `/restaurantes?ordenarPorAvaliacao=true` | Ordenar por avaliação |
| GET | `/restaurantes/{id}` | Buscar por ID |
| PUT | `/restaurantes/{id}` | Atualizar restaurante |
| DELETE | `/restaurantes/{id}` | Deletar restaurante |

**Exemplo - Criar Restaurante:**
```bash
curl -X POST http://localhost:8080/restaurantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Mania",
    "categoria": "Italiana",
    "endereco": "Av. Principal, 100",
    "telefone": "11991111111",
    "avaliacao": 4.5,
    "ativo": true,
    "horarioFuncionamento": "18:00-23:00"
  }'
```

### 🍕 Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/produtos` | Criar novo produto |
| GET | `/produtos` | Listar todos |
| GET | `/produtos?restauranteId=X` | Buscar por restaurante |
| GET | `/produtos?categoria=X` | Buscar por categoria |
| GET | `/produtos/{id}` | Buscar por ID |
| PUT | `/produtos/{id}` | Atualizar produto |
| DELETE | `/produtos/{id}` | Deletar produto |

**Exemplo - Criar Produto:**
```bash
curl -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita",
    "descricao": "Pizza tradicional com molho e mussarela",
    "preco": 45.00,
    "categoria": "Pizza",
    "disponivel": true,
    "restauranteId": 1
  }'
```

### 📦 Pedidos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/pedidos` | Criar novo pedido |
| GET | `/pedidos` | Listar todos |
| GET | `/pedidos?clienteId=X` | Buscar por cliente |
| GET | `/pedidos?status=X` | Buscar por status |
| GET | `/pedidos/{id}` | Buscar por ID |
| PATCH | `/pedidos/{id}/status?novoStatus=X` | Atualizar status |
| PATCH | `/pedidos/{id}/cancelar` | Cancelar pedido |

**Status de Pedido:**
- `PENDENTE` - Pedido criado
- `CONFIRMADO` - Pedido confirmado pelo restaurante
- `EM_PREPARACAO` - Em preparação
- `SAIU_PARA_ENTREGA` - Saiu para entrega
- `ENTREGUE` - Pedido entregue
- `CANCELADO` - Pedido cancelado

**Exemplo - Criar Pedido:**
```bash
curl -X POST http://localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "restauranteId": 1,
    "valorTotal": 55.00,
    "observacoes": "Sem cebola",
    "enderecoEntrega": "Rua A, 123"
  }'
```

**Exemplo - Atualizar Status:**
```bash
curl -X PATCH "http://localhost:8080/pedidos/1/status?novoStatus=CONFIRMADO"
```

## 🧪 Testes

### Executar Testes Unitários

```bash
./mvnw test
```

**Resultado esperado:**
```
Tests run: 40, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Executar Testes de Integração (Scripts)

O projeto inclui scripts completos para testar todos os endpoints:

**Linux/Mac:**
```bash
# Certifique-se que a aplicação está rodando
./mvnw spring-boot:run

# Em outro terminal
chmod +x test-endpoints.sh
./test-endpoints.sh
```

**Windows PowerShell:**
```powershell
# Certifique-se que a aplicação está rodando
.\mvnw.cmd spring-boot:run

# Em outro terminal
.\test-endpoints.ps1
```

Os scripts testam:
- ✅ 37 cenários de teste (bash) / 13 cenários (PowerShell)
- ✅ CRUD completo de todas as entidades
- ✅ Validações de entrada
- ✅ Tratamento de erros
- ✅ Fluxo completo de pedido

## 🎯 Casos de Uso Completos

### Cenário 1: Novo Cliente Faz um Pedido

```bash
# 1. Criar Cliente
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Santos","email":"maria@email.com","telefone":"11988888888","endereco":"Rua B, 456","ativo":true}'

# 2. Listar Restaurantes
curl http://localhost:8080/restaurantes?categoria=Italiana

# 3. Ver Produtos do Restaurante
curl http://localhost:8080/produtos?restauranteId=1

# 4. Criar Pedido
curl -X POST http://localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"restauranteId":1,"valorTotal":55.00,"enderecoEntrega":"Rua B, 456"}'

# 5. Acompanhar Status
curl http://localhost:8080/pedidos/1
```

### Cenário 2: Restaurante Gerencia Cardápio

```bash
# 1. Adicionar Novo Produto
curl -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Calzone","descricao":"Calzone recheado","preco":35.00,"categoria":"Pizza","disponivel":true,"restauranteId":1}'

# 2. Atualizar Produto
curl -X PUT http://localhost:8080/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Pizza Margherita Premium","descricao":"Com ingredientes premium","preco":50.00,"categoria":"Pizza","disponivel":true,"restauranteId":1}'

# 3. Desativar Produto
curl -X PUT http://localhost:8080/produtos/2 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Calzone","descricao":"Calzone recheado","preco":35.00,"categoria":"Pizza","disponivel":false,"restauranteId":1}'
```

### Cenário 3: Acompanhamento de Pedido

```bash
# 1. Confirmar Pedido
curl -X PATCH "http://localhost:8080/pedidos/1/status?novoStatus=CONFIRMADO"

# 2. Iniciar Preparação
curl -X PATCH "http://localhost:8080/pedidos/1/status?novoStatus=EM_PREPARACAO"

# 3. Saiu para Entrega
curl -X PATCH "http://localhost:8080/pedidos/1/status?novoStatus=SAIU_PARA_ENTREGA"

# 4. Finalizar Entrega
curl -X PATCH "http://localhost:8080/pedidos/1/status?novoStatus=ENTREGUE"
```

## ⚠️ Validações Implementadas

### Bean Validation (Jakarta)

- **@NotBlank** - Campos de texto obrigatórios
- **@Email** - Validação de formato de email
- **@Size** - Tamanho mínimo/máximo de strings
- **@DecimalMin** / **@DecimalMax** - Valores numéricos
- **@NotNull** - Campos obrigatórios

### Validações de Negócio

- ✅ Email único por cliente
- ✅ Produtos vinculados a restaurantes válidos
- ✅ Preços sempre maiores que zero
- ✅ Cancelamento apenas de pedidos não entregues
- ✅ Status de pedido seguindo fluxo válido

### Tratamento de Erros

**400 Bad Request** - Validação falhou:
```json
{
  "timestamp": "2025-10-30T23:50:00",
  "status": 400,
  "message": "Erro de validação",
  "errors": {
    "email": "Email deve ser válido",
    "nome": "Nome é obrigatório"
  }
}
```

**404 Not Found** - Recurso não encontrado:
```json
{
  "timestamp": "2025-10-30T23:50:00",
  "status": 404,
  "message": "Cliente não encontrado com ID: 999"
}
```

## 📊 Modelo de Dados

### Relacionamentos

```
Cliente 1 ─────────── * Pedido
Restaurante 1 ──────── * Produto
Restaurante 1 ──────── * Pedido
```

### Entidades Principais

- **Cliente**: id, nome, email, telefone, endereço, ativo, dataCadastro
- **Restaurante**: id, nome, categoria, endereço, telefone, avaliação, ativo
- **Produto**: id, nome, descrição, preço, categoria, disponível, restaurante
- **Pedido**: id, cliente, restaurante, data, status, valor, endereço

## 🛠️ Configuração

### application.properties

```properties
# Servidor
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:deliverydb
spring.datasource.username=sa
spring.datasource.password=

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# DevTools
spring.devtools.restart.enabled=true
```

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

Desenvolvido como projeto educacional para demonstrar:
- Arquitetura REST com Spring Boot
- Recursos modernos do Java 21
- Boas práticas de desenvolvimento
- Testes automatizados
- Documentação completa

## 🔗 Links Úteis

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Bean Validation](https://beanvalidation.org/)
- [H2 Database](https://www.h2database.com/)

## 📞 Suporte

Para reportar bugs ou solicitar features, abra uma issue no [GitHub](https://github.com/belaps/DeliveryAPI/issues).

---

**Desenvolvido com ❤️ usando Java 21 e Spring Boot 3.5.7**
