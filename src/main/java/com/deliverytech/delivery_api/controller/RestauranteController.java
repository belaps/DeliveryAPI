package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciamento de restaurantes.
 * Endpoints: POST, GET, PUT, DELETE + busca por categoria
 */
@RestController
@RequestMapping("/restaurantes")
@Validated
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    /**
     * POST /restaurantes - Criar novo restaurante
     */
    @PostMapping
    public ResponseEntity<Restaurante> criar(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        Restaurante restaurante = converterParaEntidade(restauranteDTO);
        Restaurante restauranteCriado = restauranteService.criar(restaurante);
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteCriado);
    }

    /**
     * GET /restaurantes - Listar todos os restaurantes
     * Parâmetros opcionais: categoria, nome, ordenarPorAvaliacao
     */
    @GetMapping
    public ResponseEntity<List<Restaurante>> listarTodos(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Boolean ordenarPorAvaliacao
    ) {
        List<Restaurante> restaurantes;

        if (categoria != null) {
            restaurantes = restauranteService.buscarPorCategoria(categoria);
        } else if (nome != null) {
            restaurantes = restauranteService.buscarPorNome(nome);
        } else if (ordenarPorAvaliacao != null && ordenarPorAvaliacao) {
            restaurantes = restauranteService.buscarOrdenadosPorAvaliacao();
        } else {
            restaurantes = restauranteService.listarTodos();
        }

        return ResponseEntity.ok(restaurantes);
    }

    /**
     * GET /restaurantes/{id} - Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        Restaurante restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    /**
     * GET /restaurantes/categoria/{categoria} - Buscar por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * PUT /restaurantes/{id} - Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody RestauranteDTO restauranteDTO
    ) {
        Restaurante restaurante = converterParaEntidade(restauranteDTO);
        Restaurante restauranteAtualizado = restauranteService.atualizar(id, restaurante);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    /**
     * DELETE /restaurantes/{id} - Deletar restaurante
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter DTO em Entidade
    private Restaurante converterParaEntidade(RestauranteDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.nome());
        restaurante.setCategoria(dto.categoria());
        restaurante.setEndereco(dto.endereco());
        restaurante.setTelefone(dto.telefone());
        if (dto.avaliacao() != null) {
            restaurante.setAvaliacao(dto.avaliacao());
        }
        if (dto.ativo() != null) {
            restaurante.setAtivo(dto.ativo());
        }
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        return restaurante;
    }
}
