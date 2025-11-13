package com.example.Teste.ToDo.Docker.factory;

import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaRequestDto;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaResponseDto;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.model.Tarefa;

import java.util.List;
import java.util.UUID;

public class TarefaFactory {

    public static Tarefa tarefaComId(){
        return Tarefa.builder()
                .id(UUID.fromString("3300c428-d515-424c-8f32-ea8d384fbeb9"))
                .nome("Tarefa Teste")
                .lista(ListaFactory.listaComId())
                .build();
    }

    public static TarefaResponseDto tarefaResponseDTO(){
        return TarefaResponseDto.builder()
                .id(UUID.fromString("3300c428-d515-424c-8f32-ea8d384fbeb9"))
                .nome("Tarefa Teste")
                .build();
    }

    public static TarefaRequestDto tarefaRequestDTO(){
        return TarefaRequestDto.builder()
                .nome("Tarefa Teste")
                .build();
    }

    public static List<Tarefa> listaDeTarefa(){
        return List.of(TarefaFactory.tarefaComId(),
                TarefaFactory.tarefaComId()
                        .toBuilder()
                        .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca5050"))
                        .build());
    }

    public static List<TarefaResponseDto> listaTarefaResponse(){
        return List.of(TarefaFactory.tarefaResponseDTO(),
                TarefaFactory.tarefaResponseDTO()
                        .toBuilder()
                        .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca5050"))
                        .build());
    }


}
