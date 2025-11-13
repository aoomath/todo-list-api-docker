package com.example.Teste.ToDo.Docker.factory;

import com.example.Teste.ToDo.Docker.dto.lista.ListaRequestDto;
import com.example.Teste.ToDo.Docker.dto.lista.ListaResponseDto;
import com.example.Teste.ToDo.Docker.model.Lista;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListaFactory {

    public static Lista listaComId(){
        return Lista.builder()
                .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7537"))
                .titulo("Titulo teste")
                .criacao(LocalDateTime.of(2025,7,25,16,20,10,6))
                .tarefas(new ArrayList<>())
                .build();
    }

    public static ListaRequestDto listaRequestDTO(){
        return ListaRequestDto.builder()
                .titulo("Titulo teste")
                .build();
    }

    public static ListaResponseDto listaResponseDTO(){
        return ListaResponseDto.builder()
                .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7537"))
                .titulo("Titulo teste")
                .criacao(LocalDateTime.of(2025,7,25,16,20,10,6))
                .build();
    }
    public static List<Lista> conjuntoDeLista(){
        return List.of(ListaFactory.listaComId(),
                ListaFactory.listaComId()
                        .toBuilder()
                        .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7599"))
                        .titulo("Segundo Titulo")
                        .build());
    }
    
    public static List<ListaResponseDto> conjuntoDeListaResponse(){
        return List.of(ListaFactory.listaResponseDTO(),
                ListaFactory.listaResponseDTO()
                        .toBuilder()
                        .id(UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7599"))
                        .titulo("Segundo Titulo")
                        .build());
    }
}
