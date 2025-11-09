package com.example.Teste.ToDo.Docker.dto.lista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaFilter {

    private String titulo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

}
