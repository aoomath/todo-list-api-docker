package com.example.Teste.ToDo.Docker.service;

import com.example.Teste.ToDo.Docker.dto.lista.ListaFilter;
import com.example.Teste.ToDo.Docker.dto.lista.ListaMapper;
import com.example.Teste.ToDo.Docker.dto.lista.ListaRequestDto;
import com.example.Teste.ToDo.Docker.dto.lista.ListaResponseDto;
import com.example.Teste.ToDo.Docker.exception.RecursoNaoEncontradoException;
import com.example.Teste.ToDo.Docker.factory.ListaFactory;
import com.example.Teste.ToDo.Docker.factory.PageFactory;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.repository.ListaRepository;
import com.example.Teste.ToDo.Docker.specification.ListaSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ListaServiceTeste {

    @Mock
    private ListaRepository repository;
    @Mock
    private ListaMapper mapper;

    @InjectMocks
    private ListaService service;

    private UUID idInvalido;
    private UUID id;
    private Lista listaComId;
    private ListaResponseDto response;
    private ListaRequestDto request;
    private Pageable pageable;
    private Page<Lista> page;
    private Page<ListaResponseDto> pageResponse;

    @BeforeEach
    void init(){
        id = UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7537");
        idInvalido = UUID.randomUUID();
        listaComId = ListaFactory.listaComId();
        response = ListaFactory.listaResponseDTO();
        request = ListaFactory.listaRequestDTO();
        pageable = PageRequest.of(0,10);
        page= PageFactory.listaToPage(ListaFactory.conjuntoDeLista());
        pageResponse= PageFactory.listaToPage(ListaFactory.conjuntoDeListaResponse());
    }

    // -------------------------------
    // TESTES POSITIVOS (fluxo feliz)
    // -------------------------------

    @Test
    public void deveCriarListaComSucesso(){

        Lista listaSemId = ListaFactory.listaComId()
                .toBuilder()
                .id(null)
                .criacao(null)
                .tarefas(null)
                .build();


        when(mapper.toEntity(request)).thenReturn(listaSemId);
        when(repository.save(listaSemId)).thenReturn(listaComId);
        when(mapper.toResponse(listaComId)).thenReturn(response);

        ListaResponseDto resultado = service.criar(request);

        assertNotNull(resultado.getId());
        assertEquals(response.getId(), resultado.getId());
        assertEquals(response.getTitulo(), resultado.getTitulo());
    }

    @Test
    public void deveRetornarListaQuandoIdExistir(){


        when(repository.findById(id)).thenReturn(Optional.of(listaComId));
        when(mapper.toResponse(listaComId)).thenReturn(response);

        ListaResponseDto resultado = service.buscarPeloId(id);

        assertNotNull(resultado.getId());
        assertEquals(response.getId(), resultado.getId());
        assertEquals(response.getTitulo(), resultado.getTitulo());

    }


    @Test
    public void deveRetornarUmaPaginaDeLista(){


        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toPageResponse(page)).thenReturn(pageResponse);

        Page<ListaResponseDto> resultado = service.listar(0,10);

        assertNotNull(resultado);
        assertEquals(2,resultado.getContent().size());
        assertEquals("Titulo teste", resultado.getContent().getFirst().getTitulo());

    }

    @Test
    public void deveRetornarUmaPaginaFiltradaDeLista(){

        ListaFilter filtro = new ListaFilter(null, LocalDate.of(2025,1,1),LocalDate.of(2025,12,1) );
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toPageResponse(page)).thenReturn(pageResponse);


        Page<ListaResponseDto> resultado = service.filtrar(filtro,0,10);

        assertNotNull(resultado);
        assertEquals(2,resultado.getContent().size());
    }


    @Test
    public void deveAtualizarListaQuandoIdExistir(){

        Lista listaEditadaComId = ListaFactory.listaComId().toBuilder()
                .titulo("Titulo Editado")
                .build();
        ListaRequestDto request = ListaFactory.listaRequestDTO().toBuilder()
                .titulo("Titulo Editado")
                .build();
        ListaResponseDto response = ListaFactory.listaResponseDTO().toBuilder()
                .titulo("Titulo Editado")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(listaComId));
        when(repository.save(any(Lista.class))).thenReturn(listaEditadaComId);
        when(mapper.toResponse(listaEditadaComId)).thenReturn(response);

        ListaResponseDto resultado = service.editar(request, id);

        assertEquals(resultado.getId(), listaEditadaComId.getId());
        assertEquals(request.getTitulo(), resultado.getTitulo());
    }

    @Test
    public void deveDeletarListaQuandoIdExistir(){

        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        service.deletar(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    // -------------------------------
    // TESTES DE EXCEÇÃO (fluxos alternativos)
    // -------------------------------

    @Test
    public void deveLancarExcecaoQuandoIdNaoExistir(){


        when(repository.findById(idInvalido)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPeloId(idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Lista não encontrada");
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarComIdInexistente(){

        when(repository.findById(idInvalido)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editar(request, idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Lista não encontrada");

    }

    @Test
    public void deveLancarExcecaoQuandoDeletarComIdInexistente(){

        when(repository.existsById(idInvalido)).thenReturn(false);

        assertThatThrownBy(() -> service.deletar(idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Lista não encontrada");

    }

}
