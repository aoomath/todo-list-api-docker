package com.example.Teste.ToDo.Docker.controller;

import com.example.Teste.ToDo.Docker.dto.lista.ListaFilter;
import com.example.Teste.ToDo.Docker.dto.lista.ListaRequestDto;
import com.example.Teste.ToDo.Docker.dto.lista.ListaResponseDto;
import com.example.Teste.ToDo.Docker.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/listas", produces = "application/json")
@Tag(name = "Lista", description = "Endpoints para gerenciamento de listas (criação, consulta, atualização e exclusão).")
public class ListaController {

    private final ListaService service;

    @PostMapping(consumes = "application/json")
    @Operation(summary = "Criar uma nova lista no sistema")
    @ApiResponse(responseCode = "201", description = "Lista criada com sucesso")
    public ResponseEntity<ListaResponseDto> criar(@RequestBody @Valid ListaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma lista pelo ID informado")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    public ResponseEntity<ListaResponseDto> buscarPeloId(
            @Parameter(description = "ID da lista a ser buscada", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPeloId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as listas cadastradas de forma paginada")
    @ApiResponse(responseCode = "200", description = "Listas retornadas com sucesso")
    public ResponseEntity<Page<ListaResponseDto>> listar(
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(service.listar(page, size));
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtra as listas cadastradas de forma paginada")
    @ApiResponse(responseCode = "200", description = "Listas retornadas com sucesso")
    public ResponseEntity<Page<ListaResponseDto>> filtrar(
            @Parameter(description = "Título para filtrar as listas")
            @RequestParam(required = false) String titulo,
            @Parameter(description = "Data inicial para filtrar as listas", example= "10/11/2024")
            @RequestParam(required = false)  @DateTimeFormat(pattern = "dd/MM/yyyy")  LocalDate dataInicio,
            @Parameter(description = "Data final para filtrar as listas", example= "24/04/2025")
            @RequestParam(required = false)  @DateTimeFormat(pattern = "dd/MM/yyyy")  LocalDate dataFim,
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size) {

        ListaFilter filter = ListaFilter.builder()
                .titulo(titulo)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .build();

        return ResponseEntity.ok(service.filtrar(filter, page, size));
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @Operation(summary = "Editar uma lista existente pelo ID informado")
    @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso")
    public ResponseEntity<ListaResponseDto> editar(
            @RequestBody @Valid ListaRequestDto dto,
            @Parameter(description = "ID da lista a ser editada", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id){
        return ResponseEntity.ok(service.editar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma lista pelo ID informado")
    @ApiResponse(responseCode = "204", description = "Lista removida com sucesso")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da lista a ser removida", example = "b8e2c55a-5b1a-45fa-9b59-38b89f7998b7")
            @PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
