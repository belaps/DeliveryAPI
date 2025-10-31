package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Restaurante;

/**
 * Repository para gerenciamento de restaurantes.
 * Utiliza Spring Data JPA para operações de banco de dados.
 */
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    /**
     * Busca restaurantes por nome (case insensitive).
     *
     * @param nome Nome ou parte do nome do restaurante
     * @return Lista de restaurantes correspondentes
     */
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca restaurantes por categoria.
     *
     * @param categoria Categoria do restaurante (ex: Italiana, Japonesa, etc.)
     * @return Lista de restaurantes da categoria
     */
    List<Restaurante> findByCategoria(String categoria);

    /**
     * Busca restaurantes ativos.
     *
     * @param ativo Status de ativo do restaurante
     * @return Lista de restaurantes ativos
     */
    List<Restaurante> findByAtivo(Boolean ativo);

    /**
     * Busca restaurantes ativos por categoria.
     *
     * @param categoria Categoria do restaurante
     * @param ativo Status de ativo
     * @return Lista de restaurantes ativos da categoria
     */
    List<Restaurante> findByCategoriaAndAtivo(String categoria, Boolean ativo);

    /**
     * Busca restaurantes ordenados por avaliação (maior para menor).
     * Apenas restaurantes ativos.
     *
     * @return Lista de restaurantes ordenados por avaliação
     */
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findRestaurantesAtivosPorAvaliacao();

    /**
     * Busca restaurantes com avaliação mínima.
     *
     * @param avaliacaoMinima Avaliação mínima desejada
     * @return Lista de restaurantes com avaliação >= avaliacaoMinima
     */
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true AND r.avaliacao >= :avaliacaoMinima ORDER BY r.avaliacao DESC")
    List<Restaurante> findByAvaliacaoMinimaAtivos(@Param("avaliacaoMinima") Double avaliacaoMinima);

    /**
     * Busca restaurantes por categoria ordenados por avaliação.
     *
     * @param categoria Categoria do restaurante
     * @return Lista de restaurantes da categoria ordenados por avaliação
     */
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findByCategoriaOrderByAvaliacao(@Param("categoria") String categoria);

    /**
     * Busca top N restaurantes por avaliação.
     * Utiliza LIMIT para retornar apenas os melhores.
     *
     * @return Lista dos melhores restaurantes
     */
    @Query(value = "SELECT * FROM restaurantes WHERE ativo = true ORDER BY avaliacao DESC LIMIT :limit", nativeQuery = true)
    List<Restaurante> findTopRestaurantes(@Param("limit") int limit);

    /**
     * Busca restaurantes por nome e categoria.
     *
     * @param nome Parte do nome
     * @param categoria Categoria
     * @return Lista de restaurantes correspondentes
     */
    List<Restaurante> findByNomeContainingIgnoreCaseAndCategoria(String nome, String categoria);

    /**
     * Busca restaurante por nome exato.
     *
     * @param nome Nome exato do restaurante
     * @return Optional contendo o restaurante se encontrado
     */
    Optional<Restaurante> findByNome(String nome);

    /**
     * Conta restaurantes ativos por categoria.
     *
     * @param categoria Categoria a ser contada
     * @return Número de restaurantes ativos na categoria
     */
    @Query("SELECT COUNT(r) FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true")
    Long countByCategoria(@Param("categoria") String categoria);

    /**
     * Busca todas as categorias distintas de restaurantes ativos.
     * Útil para listar filtros de categoria.
     *
     * @return Lista de categorias distintas
     */
    @Query("SELECT DISTINCT r.categoria FROM Restaurante r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDistintas();

    /**
     * Busca restaurantes com avaliação em um intervalo.
     *
     * @param min Avaliação mínima
     * @param max Avaliação máxima
     * @return Lista de restaurantes no intervalo de avaliação
     */
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true AND r.avaliacao BETWEEN :min AND :max ORDER BY r.avaliacao DESC")
    List<Restaurante> findByAvaliacaoEntre(@Param("min") Double min, @Param("max") Double max);
}
