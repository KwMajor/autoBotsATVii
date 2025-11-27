package com.autobots.automanager.controles;

import java.util.List;
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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelect;
import com.autobots.automanager.repositorios.ClienteRepository;
import com.autobots.automanager.repositorios.EnderecoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/endereco")
@Tag(name = "Endereco", description = "API de gerenciamento de endereços")
public class EnderecoController {
    // Using EnderecoSelect (not EnderecoSelecionador)
    @Autowired
    private EnderecoRepository repositorio;
    @Autowired
    private ClienteRepository clienteRepositorio;
    @Autowired
    private EnderecoSelect selecionador;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar endereço por ID", description = "Retorna um endereço específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<EntityModel<Endereco>> getEndereco(@PathVariable long id) {
        List<Endereco> enderecos = repositorio.findAll();
        Endereco endereco = selecionador.selecionar(enderecos, id);
        if (endereco == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EntityModel<Endereco> resource = EntityModel.of(endereco);
        resource.add(linkTo(methodOn(EnderecoController.class).getEndereco(id)).withSelfRel());
        resource.add(linkTo(methodOn(EnderecoController.class).getEnderecos()).withRel("enderecos"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os endereços", description = "Retorna uma lista de todos os endereços cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<Endereco>>> getEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
        List<EntityModel<Endereco>> enderecoResources = enderecos.stream()
            .map(endereco -> EntityModel.of(endereco,
                linkTo(methodOn(EnderecoController.class).getEndereco(endereco.getId())).withSelfRel(),
                linkTo(methodOn(EnderecoController.class).getEnderecos()).withRel("enderecos")))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Endereco>> collection = CollectionModel.of(enderecoResources);
        collection.add(linkTo(methodOn(EnderecoController.class).getEnderecos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @PostMapping
    @Operation(summary = "Criar novo endereço", description = "Cadastra um novo endereço no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Endereco>> createEndereco(@Valid @RequestBody Endereco endereco) {
        Endereco savedEndereco = repositorio.save(endereco);
        EntityModel<Endereco> resource = EntityModel.of(savedEndereco);
        resource.add(linkTo(methodOn(EnderecoController.class).getEndereco(savedEndereco.getId())).withSelfRel());
        resource.add(linkTo(methodOn(EnderecoController.class).getEnderecos()).withRel("enderecos"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar endereço", description = "Atualiza os dados de um endereço existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Void> updateEndereco(@PathVariable long id, @Valid @RequestBody Endereco atualizacao) {
        if (repositorio.existsById(id)) {
            Endereco endereco = repositorio.findById(id).get();
            EnderecoAtualizador atualizador = new EnderecoAtualizador();
            atualizador.atualizar(endereco, atualizacao);
            repositorio.save(endereco);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir endereço", description = "Remove um endereço do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<Void> deleteEndereco(@PathVariable long id) {
        if (!repositorio.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Remove todos endereços
        List<Cliente> clientes = clienteRepositorio.findAll();
        boolean wasRemoved = false;
        for (Cliente cliente : clientes) {
            if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(id)) {
                cliente.setEndereco(null);
                clienteRepositorio.save(cliente);
                wasRemoved = true;
            }
        }
        
        if (!wasRemoved && repositorio.existsById(id)) {
            repositorio.deleteById(id);
        }
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}