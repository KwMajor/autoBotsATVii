package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelect;
import com.autobots.automanager.repositorios.ClienteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente", description = "API de gerenciamento de clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository repository;
    @Autowired
    private ClienteSelect selecionador;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<EntityModel<Cliente>> getCliente(@PathVariable long id) {
        List<Cliente> clientes = repository.findAll();
        Cliente cliente = selecionador.selecionar(clientes, id);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EntityModel<Cliente> resource = EntityModel.of(cliente);
        resource.add(linkTo(methodOn(ClienteController.class).getCliente(id)).withSelfRel());
        resource.add(linkTo(methodOn(ClienteController.class).getClientes()).withRel("clientes"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getClientes() {
        List<Cliente> clientes = repository.findAll();
        List<EntityModel<Cliente>> clienteResources = clientes.stream()
            .map(cliente -> EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).getCliente(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getClientes()).withRel("clientes")))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Cliente>> collection = CollectionModel.of(clienteResources);
        collection.add(linkTo(methodOn(ClienteController.class).getClientes()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @PostMapping
    @Operation(summary = "Criar novo cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Cliente>> createCliente(@Valid @RequestBody Cliente cliente) {
        Cliente savedCliente = repository.save(cliente);
        EntityModel<Cliente> resource = EntityModel.of(savedCliente);
        resource.add(linkTo(methodOn(ClienteController.class).getCliente(savedCliente.getId())).withSelfRel());
        resource.add(linkTo(methodOn(ClienteController.class).getClientes()).withRel("clientes"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Void> updateCliente(@PathVariable long id, @Valid @RequestBody Cliente update) {
        Optional<Cliente> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Cliente cliente = opt.get();
        ClienteAtualizador atualizador = new ClienteAtualizador();
        atualizador.atualizar(cliente, update);
        repository.save(cliente);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> deleteCliente(@PathVariable long id) {
        Optional<Cliente> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.delete(opt.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}