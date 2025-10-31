package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;

/**
 * Service para gerenciamento de produtos.
 */
@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RestauranteService restauranteService;

    public ProdutoService(ProdutoRepository produtoRepository, RestauranteService restauranteService) {
        this.produtoRepository = produtoRepository;
        this.restauranteService = restauranteService;
    }

    public Produto criar(Produto produto, Long restauranteId) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
        produto.setRestaurante(restaurante);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());
        if (produtoAtualizado.getDisponivel() != null) {
            produto.setDisponivel(produtoAtualizado.getDisponivel());
        }
        produto.setImagemUrl(produtoAtualizado.getImagemUrl());

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    public List<Produto> buscarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findProdutosDisponiveisByRestaurante(restauranteId);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }
}
