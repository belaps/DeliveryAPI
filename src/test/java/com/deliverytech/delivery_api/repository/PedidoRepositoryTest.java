package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Pedido.StatusPedido;
import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Testes para PedidoRepository.
 */
@DataJpaTest
@DisplayName("Testes do PedidoRepository")
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Restaurante restaurante1;
    private Pedido pedido1;
    private Pedido pedido2;
    private Pedido pedido3;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        clienteRepository.deleteAll();
        restauranteRepository.deleteAll();

        // Cria clientes
        cliente1 = new Cliente("João Silva", "joao@email.com", "11999999999", "Rua A, 123");
        cliente2 = new Cliente("Maria Santos", "maria@email.com", "11988888888", "Rua B, 456");
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

        // Cria restaurante
        restaurante1 = new Restaurante("Pizza Mania", "Italiana", "Rua 1, 100", "11991111111");
        restauranteRepository.save(restaurante1);

        // Cria pedidos
        pedido1 = new Pedido(cliente1, restaurante1, new BigDecimal("100.00"), "Rua A, 123");
        pedido1.setStatus(StatusPedido.PENDENTE);

        pedido2 = new Pedido(cliente1, restaurante1, new BigDecimal("50.00"), "Rua A, 123");
        pedido2.setStatus(StatusPedido.ENTREGUE);
        pedido2.setDataEntrega(LocalDateTime.now());

        pedido3 = new Pedido(cliente2, restaurante1, new BigDecimal("75.00"), "Rua B, 456");
        pedido3.setStatus(StatusPedido.CONFIRMADO);

        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);
        pedidoRepository.save(pedido3);
    }

    @Test
    @DisplayName("Deve buscar pedidos por cliente")
    void deveBuscarPedidosPorCliente() {
        List<Pedido> pedidos = pedidoRepository.findByCliente(cliente1);

        assertThat(pedidos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar pedidos por ID do cliente")
    void deveBuscarPedidosPorClienteId() {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(cliente1.getId());

        assertThat(pedidos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar pedidos por status")
    void deveBuscarPedidosPorStatus() {
        List<Pedido> pedidos = pedidoRepository.findByStatus(StatusPedido.PENDENTE);

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getCliente().getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve buscar pedidos por cliente e status")
    void deveBuscarPedidosPorClienteEStatus() {
        List<Pedido> pedidos = pedidoRepository.findByClienteAndStatus(
            cliente1.getId(),
            StatusPedido.ENTREGUE
        );

        assertThat(pedidos).hasSize(1);
        assertThat(pedidos.get(0).getValorTotal()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Deve buscar pedidos por restaurante")
    void deveBuscarPedidosPorRestaurante() {
        List<Pedido> pedidos = pedidoRepository.findByRestauranteId(restaurante1.getId());

        assertThat(pedidos).hasSize(3);
    }

    @Test
    @DisplayName("Deve calcular total gasto por cliente")
    void deveCalcularTotalGastoPorCliente() {
        BigDecimal total = pedidoRepository.calcularTotalGastoByCliente(cliente1.getId());

        assertThat(total).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Deve calcular total de vendas por restaurante")
    void deveCalcularTotalVendasPorRestaurante() {
        BigDecimal total = pedidoRepository.calcularTotalVendasByRestaurante(restaurante1.getId());

        assertThat(total).isEqualTo(new BigDecimal("225.00"));
    }

    @Test
    @DisplayName("Deve contar pedidos por status")
    void deveContarPedidosPorStatus() {
        Long count = pedidoRepository.countByStatus(StatusPedido.PENDENTE);

        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve buscar pedidos pendentes")
    void deveBuscarPedidosPendentes() {
        List<Pedido> pedidos = pedidoRepository.findPedidosPendentes();

        assertThat(pedidos).hasSize(2); // PENDENTE e CONFIRMADO
        assertThat(pedidos).allMatch(p ->
            p.getStatus() != StatusPedido.ENTREGUE &&
            p.getStatus() != StatusPedido.CANCELADO
        );
    }

    @Test
    @DisplayName("Deve contar pedidos por cliente")
    void deveContarPedidosPorCliente() {
        Long count = pedidoRepository.countPedidosByCliente(cliente1.getId());

        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("Deve buscar pedidos acima de valor mínimo")
    void deveBuscarPedidosAcimaDeValor() {
        List<Pedido> pedidos = pedidoRepository.findPedidosAcimaDeValor(new BigDecimal("60.00"));

        assertThat(pedidos).hasSize(2);
        assertThat(pedidos).allMatch(p -> p.getValorTotal().compareTo(new BigDecimal("60.00")) >= 0);
    }

    @Test
    @DisplayName("Deve buscar pedidos por período")
    void deveBuscarPedidosPorPeriodo() {
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(1);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(dataInicio, dataFim);

        assertThat(pedidos).hasSize(3);
    }
}
