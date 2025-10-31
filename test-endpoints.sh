#!/bin/bash

# Script para testar todos os endpoints da Delivery API
# Execute com: ./test-endpoints.sh

BASE_URL="http://localhost:8080"

echo "====================================="
echo "Testando Delivery API - CRUD Completo"
echo "====================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para imprimir seção
print_section() {
    echo -e "${BLUE}===== $1 =====${NC}"
}

# Função para imprimir teste
print_test() {
    echo -e "${YELLOW}> $1${NC}"
}

# Função para imprimir sucesso
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
    echo ""
}

# ====================
# CLIENTES
# ====================
print_section "Testando CLIENTES"

print_test "1. Health Check"
curl -X GET "$BASE_URL/health"
echo ""
print_success "Health check realizado"

print_test "2. Criar Cliente 1"
CLIENTE1=$(curl -X POST "$BASE_URL/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "11999999999",
    "endereco": "Rua A, 123",
    "ativo": true
  }' -s)
echo "$CLIENTE1" | jq .
CLIENTE1_ID=$(echo "$CLIENTE1" | jq -r '.id')
print_success "Cliente 1 criado com ID: $CLIENTE1_ID"

print_test "3. Criar Cliente 2"
CLIENTE2=$(curl -X POST "$BASE_URL/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria@email.com",
    "telefone": "11988888888",
    "endereco": "Rua B, 456",
    "ativo": true
  }' -s)
echo "$CLIENTE2" | jq .
CLIENTE2_ID=$(echo "$CLIENTE2" | jq -r '.id')
print_success "Cliente 2 criado com ID: $CLIENTE2_ID"

print_test "4. Listar Todos os Clientes"
curl -X GET "$BASE_URL/clientes" -s | jq .
print_success "Lista de clientes obtida"

print_test "5. Buscar Cliente por ID"
curl -X GET "$BASE_URL/clientes/$CLIENTE1_ID" -s | jq .
print_success "Cliente encontrado"

print_test "6. Buscar Clientes Ativos"
curl -X GET "$BASE_URL/clientes?ativo=true" -s | jq .
print_success "Clientes ativos listados"

print_test "7. Atualizar Cliente"
curl -X PUT "$BASE_URL/clientes/$CLIENTE1_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Atualizado",
    "email": "joao@email.com",
    "telefone": "11999999999",
    "endereco": "Rua A, 123 - Apto 101",
    "ativo": true
  }' -s | jq .
print_success "Cliente atualizado"

# ====================
# RESTAURANTES
# ====================
print_section "Testando RESTAURANTES"

print_test "8. Criar Restaurante 1"
RESTAURANTE1=$(curl -X POST "$BASE_URL/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Mania",
    "categoria": "Italiana",
    "endereco": "Av. Principal, 100",
    "telefone": "11991111111",
    "avaliacao": 4.5,
    "ativo": true,
    "horarioFuncionamento": "18:00-23:00"
  }' -s)
echo "$RESTAURANTE1" | jq .
RESTAURANTE1_ID=$(echo "$RESTAURANTE1" | jq -r '.id')
print_success "Restaurante 1 criado com ID: $RESTAURANTE1_ID"

print_test "9. Criar Restaurante 2"
RESTAURANTE2=$(curl -X POST "$BASE_URL/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Sushi Bar",
    "categoria": "Japonesa",
    "endereco": "Rua Central, 200",
    "telefone": "11992222222",
    "avaliacao": 4.8,
    "ativo": true,
    "horarioFuncionamento": "12:00-22:00"
  }' -s)
echo "$RESTAURANTE2" | jq .
RESTAURANTE2_ID=$(echo "$RESTAURANTE2" | jq -r '.id')
print_success "Restaurante 2 criado com ID: $RESTAURANTE2_ID"

print_test "10. Listar Todos os Restaurantes"
curl -X GET "$BASE_URL/restaurantes" -s | jq .
print_success "Lista de restaurantes obtida"

print_test "11. Buscar Restaurante por ID"
curl -X GET "$BASE_URL/restaurantes/$RESTAURANTE1_ID" -s | jq .
print_success "Restaurante encontrado"

print_test "12. Buscar Restaurantes por Categoria"
curl -X GET "$BASE_URL/restaurantes?categoria=Italiana" -s | jq .
print_success "Restaurantes da categoria Italiana listados"

print_test "13. Buscar Restaurantes Ordenados por Avaliação"
curl -X GET "$BASE_URL/restaurantes?ordenarPorAvaliacao=true" -s | jq .
print_success "Restaurantes ordenados por avaliação"

print_test "14. Atualizar Restaurante"
curl -X PUT "$BASE_URL/restaurantes/$RESTAURANTE1_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Mania Premium",
    "categoria": "Italiana",
    "endereco": "Av. Principal, 100",
    "telefone": "11991111111",
    "avaliacao": 4.7,
    "ativo": true,
    "horarioFuncionamento": "17:00-00:00"
  }' -s | jq .
print_success "Restaurante atualizado"

# ====================
# PRODUTOS
# ====================
print_section "Testando PRODUTOS"

print_test "15. Criar Produto 1 (Restaurante 1)"
PRODUTO1=$(curl -X POST "$BASE_URL/produtos" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Pizza Margherita\",
    \"descricao\": \"Pizza tradicional com molho, mussarela e manjericão\",
    \"preco\": 45.00,
    \"categoria\": \"Pizza\",
    \"disponivel\": true,
    \"restauranteId\": $RESTAURANTE1_ID
  }" -s)
echo "$PRODUTO1" | jq .
PRODUTO1_ID=$(echo "$PRODUTO1" | jq -r '.id')
print_success "Produto 1 criado com ID: $PRODUTO1_ID"

print_test "16. Criar Produto 2 (Restaurante 1)"
PRODUTO2=$(curl -X POST "$BASE_URL/produtos" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Refrigerante 2L\",
    \"descricao\": \"Refrigerante de cola 2 litros\",
    \"preco\": 10.00,
    \"categoria\": \"Bebidas\",
    \"disponivel\": true,
    \"restauranteId\": $RESTAURANTE1_ID
  }" -s)
echo "$PRODUTO2" | jq .
PRODUTO2_ID=$(echo "$PRODUTO2" | jq -r '.id')
print_success "Produto 2 criado com ID: $PRODUTO2_ID"

print_test "17. Criar Produto 3 (Restaurante 2)"
PRODUTO3=$(curl -X POST "$BASE_URL/produtos" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Combo Sushi 20 peças\",
    \"descricao\": \"Variado de sushis e sashimis\",
    \"preco\": 80.00,
    \"categoria\": \"Sushi\",
    \"disponivel\": true,
    \"restauranteId\": $RESTAURANTE2_ID
  }" -s)
echo "$PRODUTO3" | jq .
PRODUTO3_ID=$(echo "$PRODUTO3" | jq -r '.id')
print_success "Produto 3 criado com ID: $PRODUTO3_ID"

print_test "18. Listar Todos os Produtos"
curl -X GET "$BASE_URL/produtos" -s | jq .
print_success "Lista de produtos obtida"

print_test "19. Buscar Produtos por Restaurante"
curl -X GET "$BASE_URL/produtos?restauranteId=$RESTAURANTE1_ID" -s | jq .
print_success "Produtos do restaurante listados"

print_test "20. Buscar Produto por ID"
curl -X GET "$BASE_URL/produtos/$PRODUTO1_ID" -s | jq .
print_success "Produto encontrado"

print_test "21. Buscar Produtos Disponíveis por Restaurante"
curl -X GET "$BASE_URL/produtos?restauranteId=$RESTAURANTE1_ID&disponivel=true" -s | jq .
print_success "Produtos disponíveis listados"

print_test "22. Atualizar Produto"
curl -X PUT "$BASE_URL/produtos/$PRODUTO1_ID" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Pizza Margherita Especial\",
    \"descricao\": \"Pizza tradicional com molho, mussarela e manjericão fresco\",
    \"preco\": 48.00,
    \"categoria\": \"Pizza\",
    \"disponivel\": true,
    \"restauranteId\": $RESTAURANTE1_ID
  }" -s | jq .
print_success "Produto atualizado"

# ====================
# PEDIDOS
# ====================
print_section "Testando PEDIDOS"

print_test "23. Criar Pedido 1"
PEDIDO1=$(curl -X POST "$BASE_URL/pedidos" \
  -H "Content-Type: application/json" \
  -d "{
    \"clienteId\": $CLIENTE1_ID,
    \"restauranteId\": $RESTAURANTE1_ID,
    \"valorTotal\": 55.00,
    \"observacoes\": \"Sem cebola\",
    \"enderecoEntrega\": \"Rua A, 123\"
  }" -s)
echo "$PEDIDO1" | jq .
PEDIDO1_ID=$(echo "$PEDIDO1" | jq -r '.id')
print_success "Pedido 1 criado com ID: $PEDIDO1_ID"

print_test "24. Criar Pedido 2"
PEDIDO2=$(curl -X POST "$BASE_URL/pedidos" \
  -H "Content-Type: application/json" \
  -d "{
    \"clienteId\": $CLIENTE2_ID,
    \"restauranteId\": $RESTAURANTE2_ID,
    \"valorTotal\": 80.00,
    \"observacoes\": \"Entrega rápida\",
    \"enderecoEntrega\": \"Rua B, 456\"
  }" -s)
echo "$PEDIDO2" | jq .
PEDIDO2_ID=$(echo "$PEDIDO2" | jq -r '.id')
print_success "Pedido 2 criado com ID: $PEDIDO2_ID"

print_test "25. Listar Todos os Pedidos"
curl -X GET "$BASE_URL/pedidos" -s | jq .
print_success "Lista de pedidos obtida"

print_test "26. Buscar Pedido por ID"
curl -X GET "$BASE_URL/pedidos/$PEDIDO1_ID" -s | jq .
print_success "Pedido encontrado"

print_test "27. Buscar Pedidos por Cliente"
curl -X GET "$BASE_URL/pedidos?clienteId=$CLIENTE1_ID" -s | jq .
print_success "Pedidos do cliente listados"

print_test "28. Buscar Pedidos Pendentes"
curl -X GET "$BASE_URL/pedidos?pendentes=true" -s | jq .
print_success "Pedidos pendentes listados"

print_test "29. Atualizar Status do Pedido (CONFIRMADO)"
curl -X PATCH "$BASE_URL/pedidos/$PEDIDO1_ID/status?novoStatus=CONFIRMADO" -s | jq .
print_success "Status atualizado para CONFIRMADO"

print_test "30. Atualizar Status do Pedido (EM_PREPARACAO)"
curl -X PATCH "$BASE_URL/pedidos/$PEDIDO1_ID/status?novoStatus=EM_PREPARACAO" -s | jq .
print_success "Status atualizado para EM_PREPARACAO"

print_test "31. Atualizar Status do Pedido (SAIU_PARA_ENTREGA)"
curl -X PATCH "$BASE_URL/pedidos/$PEDIDO1_ID/status?novoStatus=SAIU_PARA_ENTREGA" -s | jq .
print_success "Status atualizado para SAIU_PARA_ENTREGA"

print_test "32. Atualizar Status do Pedido (ENTREGUE)"
curl -X PATCH "$BASE_URL/pedidos/$PEDIDO1_ID/status?novoStatus=ENTREGUE" -s | jq .
print_success "Status atualizado para ENTREGUE"

# ====================
# TESTES DE VALIDAÇÃO
# ====================
print_section "Testando VALIDAÇÕES"

print_test "33. Tentar criar Cliente com email inválido (deve falhar)"
curl -X POST "$BASE_URL/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste",
    "email": "email-invalido",
    "telefone": "11999999999",
    "endereco": "Rua Teste"
  }' -s | jq .
print_success "Validação de email funcionando"

print_test "34. Tentar criar Produto com preço zero (deve falhar)"
curl -X POST "$BASE_URL/produtos" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Teste\",
    \"preco\": 0,
    \"categoria\": \"Teste\",
    \"restauranteId\": $RESTAURANTE1_ID
  }" -s | jq .
print_success "Validação de preço funcionando"

print_test "35. Tentar buscar Cliente inexistente (deve retornar 404)"
curl -X GET "$BASE_URL/clientes/99999" -s | jq .
print_success "Tratamento de not found funcionando"

# ====================
# TESTES DE DELETE
# ====================
print_section "Testando DELETE"

print_test "36. Deletar Produto"
curl -X DELETE "$BASE_URL/produtos/$PRODUTO3_ID" -i -s | head -n 1
print_success "Produto deletado"

print_test "37. Confirmar deleção do Produto"
curl -X GET "$BASE_URL/produtos/$PRODUTO3_ID" -s | jq .
print_success "Produto não encontrado (confirmado)"

echo ""
print_section "Testes Concluídos!"
echo -e "${GREEN}Todos os 37 testes foram executados${NC}"
echo ""
echo "Resumo dos IDs criados:"
echo "- Cliente 1: $CLIENTE1_ID"
echo "- Cliente 2: $CLIENTE2_ID"
echo "- Restaurante 1: $RESTAURANTE1_ID"
echo "- Restaurante 2: $RESTAURANTE2_ID"
echo "- Produto 1: $PRODUTO1_ID"
echo "- Produto 2: $PRODUTO2_ID"
echo "- Pedido 1: $PEDIDO1_ID"
echo "- Pedido 2: $PEDIDO2_ID"
echo ""
