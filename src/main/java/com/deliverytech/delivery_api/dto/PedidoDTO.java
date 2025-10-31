package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.entity.Pedido.StatusPedido;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para operações de Pedido.
 */
public record PedidoDTO(
    Long id,

    @NotNull(message = "ID do cliente é obrigatório")
    Long clienteId,

    @NotNull(message = "ID do restaurante é obrigatório")
    Long restauranteId,

    LocalDateTime dataPedido,

    StatusPedido status,

    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor total deve ser maior que zero")
    BigDecimal valorTotal,

    String observacoes,

    @NotBlank(message = "Endereço de entrega é obrigatório")
    String enderecoEntrega,

    LocalDateTime dataEntrega
) {
}
