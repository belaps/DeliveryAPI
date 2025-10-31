package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.deliverytech.delivery_api.entity.Cliente;

/**
 * Testes para ClienteRepository.
 * Utiliza @DataJpaTest para testes focados em JPA.
 */
@DataJpaTest
@DisplayName("Testes do ClienteRepository")
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        // Limpa o banco antes de cada teste
        clienteRepository.deleteAll();

        // Cria clientes de teste
        cliente1 = new Cliente("João Silva", "joao@email.com", "11999999999", "Rua A, 123");
        cliente2 = new Cliente("Maria Santos", "maria@email.com", "11988888888", "Rua B, 456");
        cliente2.setAtivo(false);

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
    }

    @Test
    @DisplayName("Deve buscar cliente por email")
    void deveBuscarClientePorEmail() {
        Optional<Cliente> resultado = clienteRepository.findByEmail("joao@email.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve retornar vazio quando email não existe")
    void deveRetornarVazioQuandoEmailNaoExiste() {
        Optional<Cliente> resultado = clienteRepository.findByEmail("naoexiste@email.com");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar clientes ativos")
    void deveBuscarClientesAtivos() {
        List<Cliente> ativos = clienteRepository.findByAtivo(true);

        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve buscar cliente por nome contendo string")
    void deveBuscarClientePorNomeContendo() {
        List<Cliente> resultado = clienteRepository.findByNomeContainingIgnoreCase("joão");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Deve verificar se email existe")
    void deveVerificarSeEmailExiste() {
        boolean existe = clienteRepository.existsByEmail("joao@email.com");
        boolean naoExiste = clienteRepository.existsByEmail("naoexiste@email.com");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    @DisplayName("Deve contar clientes ativos")
    void deveContarClientesAtivos() {
        Long count = clienteRepository.countClientesAtivos();

        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve buscar clientes ativos ordenados por cadastro")
    void deveBuscarClientesAtivosOrdenadosPorCadastro() {
        // Cria mais um cliente ativo
        Cliente cliente3 = new Cliente("Pedro Lima", "pedro@email.com", "11977777777", "Rua C, 789");
        clienteRepository.save(cliente3);

        List<Cliente> resultado = clienteRepository.findClientesAtivosOrdenadosPorCadastro();

        assertThat(resultado).hasSize(2);
        // Verifica que está ordenado por data de cadastro DESC (mais recente primeiro)
        assertThat(resultado.get(0).getNome()).isEqualTo("Pedro Lima");
    }

    @Test
    @DisplayName("Deve buscar cliente por telefone")
    void deveBuscarClientePorTelefone() {
        Optional<Cliente> resultado = clienteRepository.findByTelefone("11999999999");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João Silva");
    }
}
