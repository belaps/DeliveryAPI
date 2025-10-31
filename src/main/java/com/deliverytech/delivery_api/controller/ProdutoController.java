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

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.service.ProdutoService;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciamento de produtos.
 * Endpoints: POST, GET, PUT, DELETE + busca por restaurante
 */
@RestController
@RequestMapping("/produtos")
@Validated
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * POST /produtos - Criar novo produto
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = converterParaEntidade(produtoDTO);
        Produto produtoCriado = produtoService.criar(produto, produtoDTO.restauranteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    /**
     * GET /produtos - Listar todos os produtos
     * Parâmetros opcionais: restauranteId, categoria, disponivel
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos(
        @RequestParam(required = false) Long restauranteId,
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) Boolean disponivel
    ) {
        List<Produto> produtos;

        if (restauranteId != null && disponivel != null && disponivel) {
            produtos = produtoService.buscarDisponiveisPorRestaurante(restauranteId);
        } else if (restauranteId != null) {
            produtos = produtoService.buscarPorRestaurante(restauranteId);
        } else if (categoria != null) {
            produtos = produtoService.buscarPorCategoria(categoria);
        } else {
            produtos = produtoService.listarTodos();
        }

        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /produtos/{id} - Buscar produto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    /**
     * GET /produtos/restaurante/{restauranteId} - Buscar produtos por restaurante
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.buscarPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * PUT /produtos/{id} - Atualizar produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody ProdutoDTO produtoDTO
    ) {
        Produto produto = converterParaEntidade(produtoDTO);
        Produto produtoAtualizado = produtoService.atualizar(id, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * DELETE /produtos/{id} - Deletar produto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter DTO em Entidade
    private Produto converterParaEntidade(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());
        if (dto.disponivel() != null) {
            produto.setDisponivel(dto.disponivel());
        }
        produto.setImagemUrl(dto.imagemUrl());
        return produto;
    }
}
