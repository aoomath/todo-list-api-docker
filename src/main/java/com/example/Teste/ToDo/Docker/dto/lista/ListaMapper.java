package com.example.Teste.ToDo.Docker.dto.lista;

import com.example.Teste.ToDo.Docker.model.Lista;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ListaMapper {

    public Lista toEntity (ListaRequestDto request){
        Lista lista = new Lista();
        lista.setTitulo(request.getTitulo());
        return lista;
    }

    public ListaResponseDto toResponse (Lista lista){
        return new ListaResponseDto(
                lista.getId(),
                lista.getTitulo(),
                lista.getCriacao());
    }

    public Page<ListaResponseDto> toPageResponse (Page<Lista> listaPage){
        return listaPage.map(this::toResponse);
    }
}
