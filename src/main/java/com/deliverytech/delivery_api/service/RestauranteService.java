package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

/**
 * Service para gerenciamento de restaurantes.
 */
@Service
@Transactional
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante criar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com ID: " + id));
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id);

        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCategoria(restauranteAtualizado.getCategoria());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        if (restauranteAtualizado.getAvaliacao() != null) {
            restaurante.setAvaliacao(restauranteAtualizado.getAvaliacao());
        }
        if (restauranteAtualizado.getAtivo() != null) {
            restaurante.setAtivo(restauranteAtualizado.getAtivo());
        }
        restaurante.setHorarioFuncionamento(restauranteAtualizado.getHorarioFuncionamento());

        return restauranteRepository.save(restaurante);
    }

    public void deletar(Long id) {
        Restaurante restaurante = buscarPorId(id);
        restauranteRepository.delete(restaurante);
    }

    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    public List<Restaurante> buscarAtivos() {
        return restauranteRepository.findByAtivo(true);
    }

    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Restaurante> buscarOrdenadosPorAvaliacao() {
        return restauranteRepository.findRestaurantesAtivosPorAvaliacao();
    }
}
