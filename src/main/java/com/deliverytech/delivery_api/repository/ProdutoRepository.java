package com.deliverytech.delivery_api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Repository para gerenciamento de produtos.
 * Utiliza Spring Data JPA para operações de banco de dados.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca produtos por restaurante.
     *
     * @param restaurante Restaurante dono dos produtos
     * @return Lista de produtos do restaurante
     */
    List<Produto> findByRestaurante(Restaurante restaurante);

    /**
     * Busca produtos por ID do restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos do restaurante
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId")
    List<Produto> findByRestauranteId(@Param("restauranteId") Long restauranteId);

    /**
     * Busca produtos disponíveis por restaurante.
     *
     * @param restaurante Restaurante dono dos produtos
     * @param disponivel Status de disponibilidade
     * @return Lista de produtos disponíveis do restaurante
     */
    List<Produto> findByRestauranteAndDisponivel(Restaurante restaurante, Boolean disponivel);

    /**
     * Busca produtos disponíveis por ID do restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos disponíveis
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    List<Produto> findProdutosDisponiveisByRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Busca produtos por categoria.
     *
     * @param categoria Categoria do produto (ex: Bebidas, Lanches, etc.)
     * @return Lista de produtos da categoria
     */
    List<Produto> findByCategoria(String categoria);

    /**
     * Busca produtos disponíveis por categoria.
     *
     * @param categoria Categoria do produto
     * @param disponivel Status de disponibilidade
     * @return Lista de produtos disponíveis da categoria
     */
    List<Produto> findByCategoriaAndDisponivel(String categoria, Boolean disponivel);

    /**
     * Busca produtos por nome (case insensitive).
     *
     * @param nome Nome ou parte do nome do produto
     * @return Lista de produtos correspondentes
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca produtos disponíveis por nome.
     *
     * @param nome Parte do nome
     * @return Lista de produtos disponíveis correspondentes
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.disponivel = true")
    List<Produto> findProdutosDisponiveisByNome(@Param("nome") String nome);

    /**
     * Busca produtos por restaurante e categoria.
     *
     * @param restauranteId ID do restaurante
     * @param categoria Categoria do produto
     * @return Lista de produtos correspondentes
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.categoria = :categoria")
    List<Produto> findByRestauranteAndCategoria(
        @Param("restauranteId") Long restauranteId,
        @Param("categoria") String categoria
    );

    /**
     * Busca produtos disponíveis por restaurante e categoria.
     *
     * @param restauranteId ID do restaurante
     * @param categoria Categoria do produto
     * @return Lista de produtos disponíveis correspondentes
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.categoria = :categoria AND p.disponivel = true")
    List<Produto> findProdutosDisponiveisByRestauranteAndCategoria(
        @Param("restauranteId") Long restauranteId,
        @Param("categoria") String categoria
    );

    /**
     * Busca produtos com preço em um intervalo.
     *
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @return Lista de produtos no intervalo de preço
     */
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax AND p.disponivel = true ORDER BY p.preco")
    List<Produto> findByPrecoEntre(
        @Param("precoMin") BigDecimal precoMin,
        @Param("precoMax") BigDecimal precoMax
    );

    /**
     * Busca produtos por restaurante ordenados por preço.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos ordenados por preço (crescente)
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true ORDER BY p.preco ASC")
    List<Produto> findByRestauranteOrderByPreco(@Param("restauranteId") Long restauranteId);

    /**
     * Busca produtos mais caros de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @param limit Quantidade máxima de resultados
     * @return Lista dos produtos mais caros
     */
    @Query(value = "SELECT * FROM produtos WHERE restaurante_id = :restauranteId AND disponivel = true ORDER BY preco DESC LIMIT :limit", nativeQuery = true)
    List<Produto> findProdutosMaisCaros(
        @Param("restauranteId") Long restauranteId,
        @Param("limit") int limit
    );

    /**
     * Busca todas as categorias distintas de produtos disponíveis.
     *
     * @return Lista de categorias distintas
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p WHERE p.disponivel = true ORDER BY p.categoria")
    List<String> findCategoriasDistintas();

    /**
     * Busca categorias distintas de produtos de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de categorias do restaurante
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true ORDER BY p.categoria")
    List<String> findCategoriasByRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Conta produtos disponíveis de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Número de produtos disponíveis
     */
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    Long countProdutosDisponiveisByRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Busca produtos disponíveis.
     *
     * @param disponivel Status de disponibilidade
     * @return Lista de produtos disponíveis
     */
    List<Produto> findByDisponivel(Boolean disponivel);
}
