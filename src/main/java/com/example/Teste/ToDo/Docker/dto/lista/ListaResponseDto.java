package com.example.Teste.ToDo.Docker.dto.lista;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa os dados de retorno de uma lista cadastrada no sistema")
public class ListaResponseDto {

    @Schema(description = "Id da lista no formato UUID",
            example = "c74ec5bb-ad99-44d4-b7c3-d6c5c5527a5b")
    private UUID id;

    @Schema(description = "Título da lista",
            example = "Materiais Escolares")
    private String titulo;

    @Schema(description = "Data e hora de criação da lista (formato dd-MM-yyyy HH:mm:ss)",
            example = "08-11-2024 14:30:05")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime criacao;

}
