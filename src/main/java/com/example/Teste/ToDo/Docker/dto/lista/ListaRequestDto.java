package com.example.Teste.ToDo.Docker.dto.lista;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de requisição para criação ou atualização de uma lista")
public class ListaRequestDto {

    @Schema(description = "Título da lista. Esse Campo é obrigatório.",
            example = "Materiais Escolares")
    @NotBlank(message = "O título da lista é obrigatório")
    private String titulo;
}
