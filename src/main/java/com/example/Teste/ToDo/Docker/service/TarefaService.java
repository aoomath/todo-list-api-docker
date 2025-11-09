package com.example.Teste.ToDo.Docker.service;

import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaMapper;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaRequestDto;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaResponseDto;
import com.example.Teste.ToDo.Docker.exception.RecursoNaoEncontradoException;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.model.Tarefa;
import com.example.Teste.ToDo.Docker.repository.ListaRepository;
import com.example.Teste.ToDo.Docker.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TarefaService {


    private final TarefaRepository tarefaRepository;
    private final ListaRepository listaRepository;
    private final TarefaMapper mapper;



    public TarefaResponseDto criar (TarefaRequestDto dto, UUID listaId){
        Lista lista = listaRepository.findById(listaId)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Lista não encontrada"));

        Tarefa tarefa = mapper.toEntity(dto);
        tarefa.setLista(lista);
        Tarefa nova = tarefaRepository.save(tarefa);

        return mapper.toResponse(nova);
    }

    public TarefaResponseDto buscarPeloId(UUID id){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Tarefa não encontrada"));
        return mapper.toResponse(tarefa);
    }

    public List<TarefaResponseDto> listarTarefasPelaListaId(UUID listaId){
        if(!listaRepository.existsById(listaId)){
            throw new RecursoNaoEncontradoException("Lista não encontrada");
        }
        List<Tarefa> tarefa = tarefaRepository.findAllByListaId(listaId);
        return mapper.toListResponse(tarefa);
    }


    public TarefaResponseDto editar(TarefaRequestDto dto, UUID id){
        Tarefa existente = tarefaRepository.findById(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Tarefa não encontrada"));
        existente.setNome(dto.getNome());
        Tarefa editada = tarefaRepository.save(existente);
        return mapper.toResponse(editada);
    }

    public void deletar(UUID id){
        if(!tarefaRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Tarefa não encontrada");
        }
        tarefaRepository.deleteById(id);
    }

}
