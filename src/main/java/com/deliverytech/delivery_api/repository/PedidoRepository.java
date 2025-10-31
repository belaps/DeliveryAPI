package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Pedido.StatusPedido;
import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Repository para gerenciamento de pedidos.
 * Utiliza Spring Data JPA para operações de banco de dados.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca pedidos por cliente.
     *
     * @param cliente Cliente dono dos pedidos
     * @return Lista de pedidos do cliente
     */
    List<Pedido> findByCliente(Cliente cliente);

    /**
     * Busca pedidos por ID do cliente.
     *
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dataPedido DESC")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);

    /**
     * Busca pedidos por status.
     *
     * @param status Status do pedido
     * @return Lista de pedidos com o status especificado
     */
    List<Pedido> findByStatus(StatusPedido status);

    /**
     * Busca pedidos por cliente e status.
     *
     * @param clienteId ID do cliente
     * @param status Status do pedido
     * @return Lista de pedidos correspondentes
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status = :status ORDER BY p.dataPedido DESC")
    List<Pedido> findByClienteAndStatus(
        @Param("clienteId") Long clienteId,
        @Param("status") StatusPedido status
    );

    /**
     * Busca pedidos por restaurante.
     *
     * @param restaurante Restaurante
     * @return Lista de pedidos do restaurante
     */
    List<Pedido> findByRestaurante(Restaurante restaurante);

    /**
     * Busca pedidos por ID do restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de pedidos do restaurante
     */
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);

    /**
     * Busca pedidos por restaurante e status.
     *
     * @param restauranteId ID do restaurante
     * @param status Status do pedido
     * @return Lista de pedidos correspondentes
     */
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.status = :status ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteAndStatus(
        @Param("restauranteId") Long restauranteId,
        @Param("status") StatusPedido status
    );

    /**
     * Busca pedidos em um intervalo de datas.
     *
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos no período
     */
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :dataInicio AND :dataFim ORDER BY p.dataPedido DESC")
    List<Pedido> findByDataPedidoBetween(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Busca pedidos de um cliente em um período.
     *
     * @param clienteId ID do cliente
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos do cliente no período
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataPedido BETWEEN :dataInicio AND :dataFim ORDER BY p.dataPedido DESC")
    List<Pedido> findByClienteAndPeriodo(
        @Param("clienteId") Long clienteId,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Busca pedidos de um restaurante em um período.
     *
     * @param restauranteId ID do restaurante
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos do restaurante no período
     */
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.dataPedido BETWEEN :dataInicio AND :dataFim ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteAndPeriodo(
        @Param("restauranteId") Long restauranteId,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Calcula o valor total de pedidos de um cliente.
     * Relatório financeiro por cliente.
     *
     * @param clienteId ID do cliente
     * @return Valor total gasto pelo cliente
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status != 'CANCELADO'")
    BigDecimal calcularTotalGastoByCliente(@Param("clienteId") Long clienteId);

    /**
     * Calcula o valor total de pedidos de um restaurante.
     * Relatório financeiro por restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Valor total de vendas do restaurante
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.status != 'CANCELADO'")
    BigDecimal calcularTotalVendasByRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Calcula o valor total de pedidos em um período.
     * Relatório financeiro geral.
     *
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Valor total de vendas no período
     */
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.dataPedido BETWEEN :dataInicio AND :dataFim AND p.status != 'CANCELADO'")
    BigDecimal calcularTotalVendasPorPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Conta pedidos por status.
     *
     * @param status Status do pedido
     * @return Número de pedidos com o status
     */
    Long countByStatus(StatusPedido status);

    /**
     * Busca pedidos ordenados por valor (maior para menor).
     *
     * @return Lista de pedidos ordenados por valor
     */
    @Query("SELECT p FROM Pedido p ORDER BY p.valorTotal DESC")
    List<Pedido> findAllOrderByValorTotal();

    /**
     * Busca os últimos N pedidos de um cliente.
     *
     * @param clienteId ID do cliente
     * @param limit Quantidade de pedidos
     * @return Lista dos últimos pedidos
     */
    @Query(value = "SELECT * FROM pedidos WHERE cliente_id = :clienteId ORDER BY data_pedido DESC LIMIT :limit", nativeQuery = true)
    List<Pedido> findUltimosPedidosByCliente(
        @Param("clienteId") Long clienteId,
        @Param("limit") int limit
    );

    /**
     * Busca pedidos pendentes (não finalizados).
     * Útil para dashboards e monitoramento.
     *
     * @return Lista de pedidos pendentes
     */
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO', 'EM_PREPARACAO', 'SAIU_PARA_ENTREGA') ORDER BY p.dataPedido")
    List<Pedido> findPedidosPendentes();

    /**
     * Busca pedidos entregues em um período.
     * Relatório de pedidos concluídos.
     *
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos entregues
     */
    @Query("SELECT p FROM Pedido p WHERE p.status = 'ENTREGUE' AND p.dataEntrega BETWEEN :dataInicio AND :dataFim ORDER BY p.dataEntrega DESC")
    List<Pedido> findPedidosEntreguesPorPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Conta total de pedidos de um cliente.
     *
     * @param clienteId ID do cliente
     * @return Número de pedidos do cliente
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    Long countPedidosByCliente(@Param("clienteId") Long clienteId);

    /**
     * Busca pedidos com valor acima de um mínimo.
     *
     * @param valorMinimo Valor mínimo do pedido
     * @return Lista de pedidos acima do valor
     */
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal >= :valorMinimo ORDER BY p.valorTotal DESC")
    List<Pedido> findPedidosAcimaDeValor(@Param("valorMinimo") BigDecimal valorMinimo);
}
