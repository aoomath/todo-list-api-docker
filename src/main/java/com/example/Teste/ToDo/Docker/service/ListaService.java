package com.example.Teste.ToDo.Docker.service;

import com.example.Teste.ToDo.Docker.dto.lista.ListaFilter;
import com.example.Teste.ToDo.Docker.dto.lista.ListaMapper;
import com.example.Teste.ToDo.Docker.dto.lista.ListaRequestDto;
import com.example.Teste.ToDo.Docker.dto.lista.ListaResponseDto;
import com.example.Teste.ToDo.Docker.exception.RecursoNaoEncontradoException;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.repository.ListaRepository;
import com.example.Teste.ToDo.Docker.specification.ListaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ListaService {

    private final ListaRepository repository;
    private final ListaMapper mapper;



    public ListaResponseDto criar (ListaRequestDto dto){
        Lista nova = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(nova);
    }

    public ListaResponseDto buscarPeloId(UUID id){
        Lista lista = repository.findById(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Lista não encontrada"));
        return mapper.toResponse(lista);
    }

    public Page<ListaResponseDto> listar (int pagina, int tamanho){
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Lista> listas = repository.findAll(pageable);
        return mapper.toPageResponse(listas);
    }

    public Page<ListaResponseDto> filtrar(ListaFilter filter, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Lista> listas = repository.findAll(ListaSpecification.comFiltros(filter), pageable);
        return mapper.toPageResponse(listas);
    }

    public ListaResponseDto editar(ListaRequestDto dto, UUID id){
        Lista existente = repository.findById(id)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Lista não encontrada"));
        existente.setTitulo(dto.getTitulo());
        Lista editada = repository.save(existente);
        return mapper.toResponse(editada);
    }

    public void deletar (UUID id){
        if(!repository.existsById(id)){
            throw new RecursoNaoEncontradoException("Lista não encontrada");
        }
        repository.deleteById(id);
    }
}
