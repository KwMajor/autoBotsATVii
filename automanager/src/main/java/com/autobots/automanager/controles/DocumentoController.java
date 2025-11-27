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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelect;
import com.autobots.automanager.repositorios.ClienteRepository;
import com.autobots.automanager.repositorios.DocumentoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/documento")
@Tag(name = "Documento", description = "API de gerenciamento de documentos")
public class DocumentoController {
    @Autowired
    private DocumentoRepository repositorio;
    @Autowired
    private ClienteRepository clienteRepositorio;
    @Autowired
    private DocumentoSelect selecionador;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar documento por ID", description = "Retorna um documento específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    public ResponseEntity<EntityModel<Documento>> getDocumento(@PathVariable long id) {
        List<Documento> documentos = repositorio.findAll();
        Documento documento = selecionador.selecionar(documentos, id);
        if (documento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EntityModel<Documento> resource = EntityModel.of(documento);
        resource.add(linkTo(methodOn(DocumentoController.class).getDocumento(id)).withSelfRel());
        resource.add(linkTo(methodOn(DocumentoController.class).getDocumentos()).withRel("documentos"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os documentos", description = "Retorna uma lista de todos os documentos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de documentos retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<Documento>>> getDocumentos() {
        List<Documento> documentos = repositorio.findAll();
        List<EntityModel<Documento>> documentoResources = documentos.stream()
            .map(documento -> EntityModel.of(documento,
                linkTo(methodOn(DocumentoController.class).getDocumento(documento.getId())).withSelfRel(),
                linkTo(methodOn(DocumentoController.class).getDocumentos()).withRel("documentos")))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Documento>> collection = CollectionModel.of(documentoResources);
        collection.add(linkTo(methodOn(DocumentoController.class).getDocumentos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @PostMapping
    @Operation(summary = "Criar novo documento", description = "Cadastra um novo documento no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Documento>> createDocumento(@Valid @RequestBody Documento documento) {
        Documento savedDocumento = repositorio.save(documento);
        EntityModel<Documento> resource = EntityModel.of(savedDocumento);
        resource.add(linkTo(methodOn(DocumentoController.class).getDocumento(savedDocumento.getId())).withSelfRel());
        resource.add(linkTo(methodOn(DocumentoController.class).getDocumentos()).withRel("documentos"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar documento", description = "Atualiza os dados de um documento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Documento atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Void> updateDocumento(@PathVariable long id, @Valid @RequestBody Documento atualizacao) {
        if (repositorio.existsById(id)) {
            Documento documento = repositorio.findById(id).get();
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            repositorio.save(documento);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir documento", description = "Remove um documento do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Documento excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    public ResponseEntity<Void> deleteDocumento(@PathVariable long id) {
        if (!repositorio.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Cliente> clientes = clienteRepositorio.findAll();
        boolean wasRemoved = false;
        for (Cliente cliente : clientes) {
            boolean removed = cliente.getDocumentos().removeIf(doc -> doc.getId().equals(id));
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