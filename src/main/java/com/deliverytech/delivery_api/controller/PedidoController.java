package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Pedido.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoService;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciamento de pedidos.
 * Endpoints: POST (criar), GET (consultar), PATCH (atualizar status)
 */
@RestController
@RequestMapping("/pedidos")
@Validated
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * POST /pedidos - Criar novo pedido
     */
    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido = converterParaEntidade(pedidoDTO);
        Pedido pedidoCriado = pedidoService.criar(
            pedido,
            pedidoDTO.clienteId(),
            pedidoDTO.restauranteId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCriado);
    }

    /**
     * GET /pedidos - Listar todos os pedidos
     * Parâmetros opcionais: clienteId, restauranteId, status, pendentes
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos(
        @RequestParam(required = false) Long clienteId,
        @RequestParam(required = false) Long restauranteId,
        @RequestParam(required = false) StatusPedido status,
        @RequestParam(required = false) Boolean pendentes
    ) {
        List<Pedido> pedidos;

        if (clienteId != null) {
            pedidos = pedidoService.buscarPorCliente(clienteId);
        } else if (restauranteId != null) {
            pedidos = pedidoService.buscarPorRestaurante(restauranteId);
        } else if (status != null) {
            pedidos = pedidoService.buscarPorStatus(status);
        } else if (pendentes != null && pendentes) {
            pedidos = pedidoService.buscarPedidosPendentes();
        } else {
            pedidos = pedidoService.listarTodos();
        }

        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /pedidos/{id} - Buscar pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * GET /pedidos/cliente/{clienteId} - Buscar pedidos por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * PATCH /pedidos/{id}/status - Atualizar status do pedido
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(
        @PathVariable Long id,
        @RequestParam StatusPedido novoStatus
    ) {
        Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * PATCH /pedidos/{id}/cancelar - Cancelar pedido
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter DTO em Entidade
    private Pedido converterParaEntidade(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setValorTotal(dto.valorTotal());
        pedido.setObservacoes(dto.observacoes());
        pedido.setEnderecoEntrega(dto.enderecoEntrega());
        return pedido;
    }
}
