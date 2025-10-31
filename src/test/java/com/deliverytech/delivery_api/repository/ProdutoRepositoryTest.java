package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Testes para ProdutoRepository.
 */
@DataJpaTest
@DisplayName("Testes do ProdutoRepository")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();

        // Cria restaurantes
        restaurante1 = new Restaurante("Pizza Mania", "Italiana", "Rua 1, 100", "11991111111");
        restaurante2 = new Restaurante("Sushi Bar", "Japonesa", "Rua 2, 200", "11992222222");
        restauranteRepository.save(restaurante1);
        restauranteRepository.save(restaurante2);

        // Cria produtos
        produto1 = new Produto("Pizza Margherita", "Pizza clássica", new BigDecimal("45.00"), "Pizza", restaurante1);
        produto2 = new Produto("Coca-Cola", "Refrigerante 2L", new BigDecimal("10.00"), "Bebidas", restaurante1);
        produto3 = new Produto("Sushi Combo", "Combo de sushi", new BigDecimal("80.00"), "Sushi", restaurante2);
        produto3.setDisponivel(false);

        produtoRepository.save(produto1);
        produtoRepository.save(produto2);
        produtoRepository.save(produto3);
    }

    @Test
    @DisplayName("Deve buscar produtos por restaurante")
    void deveBuscarProdutosPorRestaurante() {
        List<Produto> produtos = produtoRepository.findByRestaurante(restaurante1);

        assertThat(produtos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar produtos por ID do restaurante")
    void deveBuscarProdutosPorRestauranteId() {
        List<Produto> produtos = produtoRepository.findByRestauranteId(restaurante1.getId());

        assertThat(produtos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar apenas produtos disponíveis")
    void deveBuscarProdutosDisponiveis() {
        List<Produto> disponiveis = produtoRepository.findProdutosDisponiveisByRestaurante(restaurante2.getId());

        assertThat(disponiveis).isEmpty(); // Produto do restaurante2 está indisponível
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria")
    void deveBuscarProdutosPorCategoria() {
        List<Produto> produtos = produtoRepository.findByCategoria("Pizza");

        assertThat(produtos).hasSize(1);
        assertThat(produtos.get(0).getNome()).isEqualTo("Pizza Margherita");
    }

    @Test
    @DisplayName("Deve buscar produtos por nome")
    void deveBuscarProdutosPorNome() {
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase("pizza");

        assertThat(produtos).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar produtos por restaurante e categoria")
    void deveBuscarProdutosPorRestauranteECategoria() {
        List<Produto> produtos = produtoRepository.findByRestauranteAndCategoria(
            restaurante1.getId(),
            "Bebidas"
        );

        assertThat(produtos).hasSize(1);
        assertThat(produtos.get(0).getNome()).isEqualTo("Coca-Cola");
    }

    @Test
    @DisplayName("Deve buscar produtos por intervalo de preço")
    void deveBuscarProdutosPorIntervaloDePreco() {
        List<Produto> produtos = produtoRepository.findByPrecoEntre(
            new BigDecimal("10.00"),
            new BigDecimal("50.00")
        );

        assertThat(produtos).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar categorias distintas")
    void deveBuscarCategoriasDistintas() {
        List<String> categorias = produtoRepository.findCategoriasDistintas();

        assertThat(categorias).hasSize(2); // Pizza e Bebidas (Sushi está indisponível)
        assertThat(categorias).contains("Pizza", "Bebidas");
    }

    @Test
    @DisplayName("Deve contar produtos disponíveis por restaurante")
    void deveContarProdutosDisponiveisPorRestaurante() {
        Long count = produtoRepository.countProdutosDisponiveisByRestaurante(restaurante1.getId());

        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("Deve buscar produtos ordenados por preço")
    void deveBuscarProdutosOrdenadosPorPreco() {
        List<Produto> produtos = produtoRepository.findByRestauranteOrderByPreco(restaurante1.getId());

        assertThat(produtos).hasSize(2);
        assertThat(produtos.get(0).getPreco()).isEqualTo(new BigDecimal("10.00"));
        assertThat(produtos.get(1).getPreco()).isEqualTo(new BigDecimal("45.00"));
    }
}
