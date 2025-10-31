package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.entity.Cliente;

/**
 * Repository para gerenciamento de clientes.
 * Utiliza Spring Data JPA para operações de banco de dados.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca cliente por e-mail.
     * Método de consulta derivado do nome (Derived Query Method).
     *
     * @param email E-mail do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Busca todos os clientes ativos.
     *
     * @param ativo Status de ativo do cliente
     * @return Lista de clientes ativos
     */
    List<Cliente> findByAtivo(Boolean ativo);

    /**
     * Busca clientes por nome contendo a string fornecida (case insensitive).
     *
     * @param nome Parte do nome a ser buscado
     * @return Lista de clientes correspondentes
     */
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca clientes ativos por nome.
     * Combina dois critérios de busca.
     *
     * @param nome Nome ou parte do nome
     * @param ativo Status de ativo
     * @return Lista de clientes ativos correspondentes
     */
    List<Cliente> findByNomeContainingIgnoreCaseAndAtivo(String nome, Boolean ativo);

    /**
     * Verifica se existe um cliente com o e-mail fornecido.
     *
     * @param email E-mail a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Busca clientes ordenados por data de cadastro (mais recentes primeiro).
     * Utiliza @Query para consulta JPQL customizada.
     *
     * @return Lista de clientes ordenados
     */
    @Query("SELECT c FROM Cliente c WHERE c.ativo = true ORDER BY c.dataCadastro DESC")
    List<Cliente> findClientesAtivosOrdenadosPorCadastro();

    /**
     * Conta quantos clientes ativos existem.
     *
     * @return Número de clientes ativos
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    Long countClientesAtivos();

    /**
     * Busca clientes por telefone.
     *
     * @param telefone Telefone do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByTelefone(String telefone);

    /**
     * Busca clientes cadastrados após uma determinada data.
     * Utiliza named parameter para melhor legibilidade.
     *
     * @param dataInicio Data inicial para filtro
     * @return Lista de clientes cadastrados após a data
     */
    @Query("SELECT c FROM Cliente c WHERE c.dataCadastro >= :dataInicio ORDER BY c.dataCadastro DESC")
    List<Cliente> findClientesCadastradosApos(@Param("dataInicio") java.time.LocalDateTime dataInicio);
}
