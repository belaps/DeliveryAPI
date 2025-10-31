# Script PowerShell para testar todos os endpoints da Delivery API
# Execute com: .\test-endpoints.ps1

$BASE_URL = "http://localhost:8080"

Write-Host "=====================================" -ForegroundColor Blue
Write-Host "Testando Delivery API - CRUD Completo" -ForegroundColor Blue
Write-Host "=====================================" -ForegroundColor Blue
Write-Host ""

function Print-Section {
    param($text)
    Write-Host "===== $text =====" -ForegroundColor Blue
}

function Print-Test {
    param($text)
    Write-Host "> $text" -ForegroundColor Yellow
}

function Print-Success {
    param($text)
    Write-Host "✓ $text" -ForegroundColor Green
    Write-Host ""
}

# ====================
# CLIENTES
# ====================
Print-Section "Testando CLIENTES"

Print-Test "1. Health Check"
$response = Invoke-RestMethod -Uri "$BASE_URL/health" -Method Get
$response | ConvertTo-Json
Print-Success "Health check realizado"

Print-Test "2. Criar Cliente 1"
$cliente1 = @{
    nome = "João Silva"
    email = "joao@email.com"
    telefone = "11999999999"
    endereco = "Rua A, 123"
    ativo = $true
} | ConvertTo-Json
$responseCliente1 = Invoke-RestMethod -Uri "$BASE_URL/clientes" -Method Post -Body $cliente1 -ContentType "application/json"
$responseCliente1 | ConvertTo-Json
$CLIENTE1_ID = $responseCliente1.id
Print-Success "Cliente 1 criado com ID: $CLIENTE1_ID"

Print-Test "3. Criar Cliente 2"
$cliente2 = @{
    nome = "Maria Santos"
    email = "maria@email.com"
    telefone = "11988888888"
    endereco = "Rua B, 456"
    ativo = $true
} | ConvertTo-Json
$responseCliente2 = Invoke-RestMethod -Uri "$BASE_URL/clientes" -Method Post -Body $cliente2 -ContentType "application/json"
$responseCliente2 | ConvertTo-Json
$CLIENTE2_ID = $responseCliente2.id
Print-Success "Cliente 2 criado com ID: $CLIENTE2_ID"

Print-Test "4. Listar Todos os Clientes"
$clientes = Invoke-RestMethod -Uri "$BASE_URL/clientes" -Method Get
$clientes | ConvertTo-Json
Print-Success "Lista de clientes obtida"

Print-Test "5. Buscar Cliente por ID"
$cliente = Invoke-RestMethod -Uri "$BASE_URL/clientes/$CLIENTE1_ID" -Method Get
$cliente | ConvertTo-Json
Print-Success "Cliente encontrado"

# ====================
# RESTAURANTES
# ====================
Print-Section "Testando RESTAURANTES"

Print-Test "6. Criar Restaurante 1"
$restaurante1 = @{
    nome = "Pizza Mania"
    categoria = "Italiana"
    endereco = "Av. Principal, 100"
    telefone = "11991111111"
    avaliacao = 4.5
    ativo = $true
    horarioFuncionamento = "18:00-23:00"
} | ConvertTo-Json
$responseRestaurante1 = Invoke-RestMethod -Uri "$BASE_URL/restaurantes" -Method Post -Body $restaurante1 -ContentType "application/json"
$responseRestaurante1 | ConvertTo-Json
$RESTAURANTE1_ID = $responseRestaurante1.id
Print-Success "Restaurante 1 criado com ID: $RESTAURANTE1_ID"

Print-Test "7. Criar Restaurante 2"
$restaurante2 = @{
    nome = "Sushi Bar"
    categoria = "Japonesa"
    endereco = "Rua Central, 200"
    telefone = "11992222222"
    avaliacao = 4.8
    ativo = $true
    horarioFuncionamento = "12:00-22:00"
} | ConvertTo-Json
$responseRestaurante2 = Invoke-RestMethod -Uri "$BASE_URL/restaurantes" -Method Post -Body $restaurante2 -ContentType "application/json"
$responseRestaurante2 | ConvertTo-Json
$RESTAURANTE2_ID = $responseRestaurante2.id
Print-Success "Restaurante 2 criado com ID: $RESTAURANTE2_ID"

Print-Test "8. Buscar Restaurantes por Categoria"
$restaurantes = Invoke-RestMethod -Uri "$BASE_URL/restaurantes?categoria=Italiana" -Method Get
$restaurantes | ConvertTo-Json
Print-Success "Restaurantes da categoria Italiana listados"

# ====================
# PRODUTOS
# ====================
Print-Section "Testando PRODUTOS"

Print-Test "9. Criar Produto 1"
$produto1 = @{
    nome = "Pizza Margherita"
    descricao = "Pizza tradicional com molho, mussarela e manjericão"
    preco = 45.00
    categoria = "Pizza"
    disponivel = $true
    restauranteId = $RESTAURANTE1_ID
} | ConvertTo-Json
$responseProduto1 = Invoke-RestMethod -Uri "$BASE_URL/produtos" -Method Post -Body $produto1 -ContentType "application/json"
$responseProduto1 | ConvertTo-Json
$PRODUTO1_ID = $responseProduto1.id
Print-Success "Produto 1 criado com ID: $PRODUTO1_ID"

Print-Test "10. Buscar Produtos por Restaurante"
$produtos = Invoke-RestMethod -Uri "$BASE_URL/produtos?restauranteId=$RESTAURANTE1_ID" -Method Get
$produtos | ConvertTo-Json
Print-Success "Produtos do restaurante listados"

# ====================
# PEDIDOS
# ====================
Print-Section "Testando PEDIDOS"

Print-Test "11. Criar Pedido 1"
$pedido1 = @{
    clienteId = $CLIENTE1_ID
    restauranteId = $RESTAURANTE1_ID
    valorTotal = 55.00
    observacoes = "Sem cebola"
    enderecoEntrega = "Rua A, 123"
} | ConvertTo-Json
$responsePedido1 = Invoke-RestMethod -Uri "$BASE_URL/pedidos" -Method Post -Body $pedido1 -ContentType "application/json"
$responsePedido1 | ConvertTo-Json
$PEDIDO1_ID = $responsePedido1.id
Print-Success "Pedido 1 criado com ID: $PEDIDO1_ID"

Print-Test "12. Buscar Pedidos por Cliente"
$pedidos = Invoke-RestMethod -Uri "$BASE_URL/pedidos?clienteId=$CLIENTE1_ID" -Method Get
$pedidos | ConvertTo-Json
Print-Success "Pedidos do cliente listados"

Print-Test "13. Atualizar Status do Pedido"
$pedidoAtualizado = Invoke-RestMethod -Uri "$BASE_URL/pedidos/$PEDIDO1_ID/status?novoStatus=CONFIRMADO" -Method Patch
$pedidoAtualizado | ConvertTo-Json
Print-Success "Status atualizado para CONFIRMADO"

Write-Host ""
Print-Section "Testes Concluídos!"
Write-Host "Todos os testes foram executados com sucesso" -ForegroundColor Green
Write-Host ""
Write-Host "Resumo dos IDs criados:"
Write-Host "- Cliente 1: $CLIENTE1_ID"
Write-Host "- Cliente 2: $CLIENTE2_ID"
Write-Host "- Restaurante 1: $RESTAURANTE1_ID"
Write-Host "- Restaurante 2: $RESTAURANTE2_ID"
Write-Host "- Produto 1: $PRODUTO1_ID"
Write-Host "- Pedido 1: $PEDIDO1_ID"
Write-Host ""
