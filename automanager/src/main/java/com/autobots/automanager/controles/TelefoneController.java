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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelect;
import com.autobots.automanager.repositorios.ClienteRepository;
import com.autobots.automanager.repositorios.TelefoneRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;



@RestController
@RequestMapping("/telefone")
@Tag(name = "Telefone", description = "API de gerenciamento de telefones")
public class TelefoneController {
    // Using TelefoneSelect (not TelefoneSelecionador)
    @Autowired
    private TelefoneRepository repositorio;
    @Autowired
    private ClienteRepository clienteRepositorio;
    @Autowired
    private TelefoneSelect selecionador;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar telefone por ID", description = "Retorna um telefone específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Telefone encontrado"),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    })
    public ResponseEntity<EntityModel<Telefone>> getTelefone(@PathVariable long id) {
        List<Telefone> telefones = repositorio.findAll();
        Telefone telefone = selecionador.selecionar(telefones, id);
        if (telefone == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EntityModel<Telefone> resource = EntityModel.of(telefone);
        resource.add(linkTo(methodOn(TelefoneController.class).getTelefone(id)).withSelfRel());
        resource.add(linkTo(methodOn(TelefoneController.class).getTelefones()).withRel("telefones"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os telefones", description = "Retorna uma lista de todos os telefones cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de telefones retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<Telefone>>> getTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        List<EntityModel<Telefone>> telefoneResources = telefones.stream()
            .map(telefone -> EntityModel.of(telefone,
                linkTo(methodOn(TelefoneController.class).getTelefone(telefone.getId())).withSelfRel(),
                linkTo(methodOn(TelefoneController.class).getTelefones()).withRel("telefones")))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Telefone>> collection = CollectionModel.of(telefoneResources);
        collection.add(linkTo(methodOn(TelefoneController.class).getTelefones()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @PostMapping
    @Operation(summary = "Criar novo telefone", description = "Cadastra um novo telefone no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Telefone criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Telefone>> createTelefone(@Valid @RequestBody Telefone telefone) {
        Telefone savedTelefone = repositorio.save(telefone);
        EntityModel<Telefone> resource = EntityModel.of(savedTelefone);
        resource.add(linkTo(methodOn(TelefoneController.class).getTelefone(savedTelefone.getId())).withSelfRel());
        resource.add(linkTo(methodOn(TelefoneController.class).getTelefones()).withRel("telefones"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar telefone", description = "Atualiza os dados de um telefone existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Telefone atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Void> updateTelefone(@PathVariable long id, @Valid @RequestBody Telefone atualizacao) {
        if (repositorio.existsById(id)) {
            Telefone telefone = repositorio.findById(id).get();
            TelefoneAtualizador atualizador = new TelefoneAtualizador();
            atualizador.atualizar(telefone, atualizacao);
            repositorio.save(telefone);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir telefone", description = "Remove um telefone do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Telefone excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    })
    public ResponseEntity<Void> deleteTelefone(@PathVariable long id) {
        if (!repositorio.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Cliente> clientes = clienteRepositorio.findAll();
        boolean wasRemoved = false;
        for (Cliente cliente : clientes) {
            boolean removed = cliente.getTelefones().removeIf(tel -> tel.getId().equals(id));
            if (removed) {
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