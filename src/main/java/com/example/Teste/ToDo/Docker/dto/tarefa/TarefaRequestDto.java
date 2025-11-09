package com.example.Teste.ToDo.Docker.dto.tarefa;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
@Schema(description = "Objeto de requisição para criação ou atualização de uma tarefa")
public class TarefaRequestDto {

    @Schema(description = "Nome da tarefa",
            example = "Comprar caderno de desenho")
    @NotBlank(message = "O nome da tarefa é obrigatório")
    private String nome;
}
