package com.example.Teste.ToDo.Docker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ListaRequestDto {

    @NotBlank(message = "O nome da lista é obrigatório")
    private String nome;
}
