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

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.service.ClienteService;

import jakarta.validation.Valid;

/**
 * Controller REST para gerenciamento de clientes.
 * Endpoints: POST, GET, PUT, DELETE
 */
@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * POST /clientes - Criar novo cliente
     */
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = converterParaEntidade(clienteDTO);
        Cliente clienteCriado = clienteService.criar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }

    /**
     * GET /clientes - Listar todos os clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Boolean ativo
    ) {
        List<Cliente> clientes;

        if (nome != null) {
            clientes = clienteService.buscarPorNome(nome);
        } else if (ativo != null && ativo) {
            clientes = clienteService.buscarAtivos();
        } else {
            clientes = clienteService.listarTodos();
        }

        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /clientes/{id} - Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * PUT /clientes/{id} - Atualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody ClienteDTO clienteDTO
    ) {
        Cliente cliente = converterParaEntidade(clienteDTO);
        Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    /**
     * DELETE /clientes/{id} - Deletar cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter DTO em Entidade
    private Cliente converterParaEntidade(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setEndereco(dto.endereco());
        if (dto.ativo() != null) {
            cliente.setAtivo(dto.ativo());
        }
        return cliente;
    }
}
