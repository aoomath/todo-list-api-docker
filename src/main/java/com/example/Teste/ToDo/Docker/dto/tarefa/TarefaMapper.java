package com.example.Teste.ToDo.Docker.dto.tarefa;

import com.example.Teste.ToDo.Docker.model.Tarefa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TarefaMapper {

    public Tarefa toEntity (TarefaRequestDto dto){
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(dto.getNome());

        return tarefa;
    }

    public TarefaResponseDto toResponse (Tarefa tarefa){
        return new TarefaResponseDto(tarefa.getId(), tarefa.getNome());
    }

    public List<TarefaResponseDto> toListResponse (List<Tarefa> tarefa){
        return tarefa.stream().map(this::toResponse).toList();
    }
}
