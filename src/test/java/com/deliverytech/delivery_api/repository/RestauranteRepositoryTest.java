package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Testes para RestauranteRepository.
 */
@DataJpaTest
@DisplayName("Testes do RestauranteRepository")
class RestauranteRepositoryTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private Restaurante restaurante3;

    @BeforeEach
    void setUp() {
        restauranteRepository.deleteAll();

        restaurante1 = new Restaurante("Pizza Mania", "Italiana", "Rua 1, 100", "11991111111");
        restaurante1.setAvaliacao(4.5);

        restaurante2 = new Restaurante("Sushi Bar", "Japonesa", "Rua 2, 200", "11992222222");
        restaurante2.setAvaliacao(4.8);

        restaurante3 = new Restaurante("Burger House", "Hamburguer", "Rua 3, 300", "11993333333");
        restaurante3.setAvaliacao(4.2);
        restaurante3.setAtivo(false);

        restauranteRepository.save(restaurante1);
        restauranteRepository.save(restaurante2);
        restauranteRepository.save(restaurante3);
    }

    @Test
    @DisplayName("Deve buscar restaurantes por nome contendo string")
    void deveBuscarRestaurantesPorNome() {
        List<Restaurante> resultado = restauranteRepository.findByNomeContainingIgnoreCase("pizza");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Pizza Mania");
    }

    @Test
    @DisplayName("Deve buscar restaurantes por categoria")
    void deveBuscarRestaurantesPorCategoria() {
        List<Restaurante> resultado = restauranteRepository.findByCategoria("Japonesa");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Sushi Bar");
    }

    @Test
    @DisplayName("Deve buscar apenas restaurantes ativos")
    void deveBuscarRestaurantesAtivos() {
        List<Restaurante> ativos = restauranteRepository.findByAtivo(true);

        assertThat(ativos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar restaurantes ordenados por avaliação")
    void deveBuscarRestaurantesOrdenadosPorAvaliacao() {
        List<Restaurante> resultado = restauranteRepository.findRestaurantesAtivosPorAvaliacao();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getAvaliacao()).isEqualTo(4.8);
        assertThat(resultado.get(0).getNome()).isEqualTo("Sushi Bar");
    }

    @Test
    @DisplayName("Deve buscar restaurantes com avaliação mínima")
    void deveBuscarRestaurantesComAvaliacaoMinima() {
        List<Restaurante> resultado = restauranteRepository.findByAvaliacaoMinimaAtivos(4.5);

        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(r -> r.getAvaliacao() >= 4.5);
    }

    @Test
    @DisplayName("Deve buscar categorias distintas")
    void deveBuscarCategoriasDistintas() {
        List<String> categorias = restauranteRepository.findCategoriasDistintas();

        assertThat(categorias).hasSize(2);
        assertThat(categorias).contains("Italiana", "Japonesa");
        assertThat(categorias).doesNotContain("Hamburguer"); // Está inativo
    }

    @Test
    @DisplayName("Deve contar restaurantes por categoria")
    void deveContarRestaurantesPorCategoria() {
        Long count = restauranteRepository.countByCategoria("Italiana");

        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve buscar restaurantes por categoria ordenados por avaliação")
    void deveBuscarPorCategoriaOrdenadosPorAvaliacao() {
        // Adiciona mais um restaurante italiano
        Restaurante restaurante4 = new Restaurante("Pasta & Co", "Italiana", "Rua 4, 400", "11994444444");
        restaurante4.setAvaliacao(4.9);
        restauranteRepository.save(restaurante4);

        List<Restaurante> resultado = restauranteRepository.findByCategoriaOrderByAvaliacao("Italiana");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getAvaliacao()).isEqualTo(4.9);
    }
}
