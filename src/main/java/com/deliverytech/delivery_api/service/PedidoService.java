package com.deliverytech.delivery_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Pedido.StatusPedido;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.PedidoRepository;

/**
 * Service para gerenciamento de pedidos.
 */
@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final RestauranteService restauranteService;

    public PedidoService(PedidoRepository pedidoRepository,
                        ClienteService clienteService,
                        RestauranteService restauranteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.restauranteService = restauranteService;
    }

    public Pedido criar(Pedido pedido, Long clienteId, Long restauranteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId);

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + id));
    }

    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);

        // Se o status for ENTREGUE, registra a data de entrega
        if (novoStatus == StatusPedido.ENTREGUE) {
            pedido.setDataEntrega(LocalDateTime.now());
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPedidosPendentes() {
        return pedidoRepository.findPedidosPendentes();
    }

    public void cancelar(Long id) {
        Pedido pedido = buscarPorId(id);

        // Só permite cancelar pedidos que não foram entregues
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Não é possível cancelar um pedido já entregue");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
