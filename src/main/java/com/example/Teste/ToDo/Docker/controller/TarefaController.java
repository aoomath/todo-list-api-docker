package com.example.Teste.ToDo.Docker.controller;

import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaRequestDto;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaResponseDto;
import com.example.Teste.ToDo.Docker.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/tarefas", produces = "application/json")
@Tag(name = "Tarefa", description = "Endpoints para gerenciamento de tarefas (criação, consulta, atualização e exclusão).")

public class TarefaController {

    private final TarefaService service;


    @PostMapping("/listas/{listaId}")
    @Operation(summary = "Criar uma nova tarefa em uma lista")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    public ResponseEntity<TarefaResponseDto> criar(
            @RequestBody @Valid TarefaRequestDto dto,
            @Parameter(description = "ID da lista onde a tarefa vai ser criada", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID listaId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto, listaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma tarefa pelo ID informado")
    @ApiResponse(responseCode = "200", description = "Tarefa encontrada")
    public ResponseEntity<TarefaResponseDto> buscarPeloId(
            @Parameter(description = "ID da tarefa a ser buscada", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id){
        return ResponseEntity.ok(service.buscarPeloId(id));
    }

    @GetMapping("/listas/{listaId}")
    @Operation(summary = "Listar todas as tarefas de uma lista específica")
    @ApiResponse(responseCode = "200", description = "Tarefa retornadas com sucesso")
    public ResponseEntity<List<TarefaResponseDto>> listar(
            @Parameter(description = "ID da lista para buscar tarefas", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID listaId){
        return ResponseEntity.ok(service.listarTarefasPelaListaId(listaId));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Editar uma tarefa existente pelo ID informado")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    public ResponseEntity<TarefaResponseDto> editar(
            @RequestBody @Valid TarefaRequestDto dto,
            @Parameter(description = "ID da tarefa a ser editada", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id){
        return ResponseEntity.ok(service.editar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma tarefa pelo ID informado")
    @ApiResponse(responseCode = "204", description = "Tarefa removida com sucesso")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da tarefa a ser removida", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
