package com.example.Teste.ToDo.Docker.dto.tarefa;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
@Schema(description = "Representa os dados de retorno de uma tarefa cadastrada no sistema")
public class TarefaResponseDto {

    @Schema(description = "Id da tarefa no formato UUID",
            example = "c74ec5bb-ad99-44d4-b7c3-d6c5c5527a5b")
    private UUID id;
    @Schema(description = "Nome da tarefa",
            example = "Comprar caderno de desenho")
    private String nome;
}
