package com.deliverytech.delivery_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para operações de Restaurante.
 */
public record RestauranteDTO(
    Long id,

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String nome,

    @NotBlank(message = "Categoria é obrigatória")
    String categoria,

    @NotBlank(message = "Endereço é obrigatório")
    String endereco,

    String telefone,

    @DecimalMin(value = "0.0", message = "Avaliação deve ser no mínimo 0.0")
    @DecimalMax(value = "5.0", message = "Avaliação deve ser no máximo 5.0")
    Double avaliacao,

    Boolean ativo,

    String horarioFuncionamento
) {
}
