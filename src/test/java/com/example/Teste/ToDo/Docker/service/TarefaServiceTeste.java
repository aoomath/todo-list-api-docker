package com.example.Teste.ToDo.Docker.service;

import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaMapper;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaRequestDto;
import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaResponseDto;
import com.example.Teste.ToDo.Docker.exception.RecursoNaoEncontradoException;
import com.example.Teste.ToDo.Docker.factory.ListaFactory;
import com.example.Teste.ToDo.Docker.factory.TarefaFactory;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.model.Tarefa;
import com.example.Teste.ToDo.Docker.repository.ListaRepository;
import com.example.Teste.ToDo.Docker.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTeste {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private TarefaMapper mapper;

    @InjectMocks
    private TarefaService service;


    private UUID id;
    private UUID idInvalido;
    private UUID listaId;
    private UUID listaIdInvalido;

    private Lista lista;
    private Tarefa tarefaComLista;
    private List<Tarefa> listaTarefa;
    private TarefaRequestDto request;
    private TarefaResponseDto response;
    private List<TarefaResponseDto> listaResponse;

    @BeforeEach
    void init(){
        id = UUID.fromString("3300c428-d515-424c-8f32-ea8d384fbeb9");
        idInvalido = UUID.randomUUID();
        listaId = UUID.fromString("0b1cd2ea-084d-4d71-803e-d81fe4ca7537");
        listaIdInvalido = UUID.randomUUID();

        lista = ListaFactory.listaComId();
        tarefaComLista = TarefaFactory.tarefaComId();
        listaTarefa = TarefaFactory.listaDeTarefa();

        request = TarefaFactory.tarefaRequestDTO();
        response = TarefaFactory.tarefaResponseDTO();
        listaResponse = TarefaFactory.listaTarefaResponse();

    }

    // -------------------------------
    // TESTES POSITIVOS (fluxo feliz)
    // -------------------------------

    @Test
    public void deveCriarTarefaNaListaComSucesso(){

        Tarefa tarefaSemLista = TarefaFactory.tarefaComId().toBuilder()
                .id(null)
                .lista(null)
                .build();

        when(listaRepository.findById(listaId)).thenReturn(Optional.of(lista));
        when(mapper.toEntity(request)).thenReturn(tarefaSemLista);
        when(tarefaRepository.save(tarefaSemLista)).thenReturn(tarefaComLista);
        when(mapper.toResponse(tarefaComLista)).thenReturn(response);

        TarefaResponseDto resultado = service.criar(request,listaId);

        assertNotNull(resultado.getId());
        assertEquals(lista, tarefaComLista.getLista());
        assertEquals(tarefaComLista.getId(), resultado.getId());
        assertEquals(request.getNome(), resultado.getNome());

    }


    @Test
    public void deveRetornarUmaTarefaQuandoIdExistir(){

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefaComLista));
        when(mapper.toResponse(tarefaComLista)).thenReturn(response);

        TarefaResponseDto resultado = service.buscarPeloId(id);

        assertNotNull(resultado.getId());
        assertEquals(tarefaComLista.getId(), resultado.getId());

    }

    @Test
    public void deveRetornarUmaListaDeTarefaComSucesso(){


        when(listaRepository.existsById(listaId)).thenReturn(true);
        when(tarefaRepository.findAllByListaId(listaId)).thenReturn(listaTarefa);
        when(mapper.toListResponse(listaTarefa)).thenReturn(listaResponse);

        List<TarefaResponseDto> resultado = service.listarTarefasPelaListaId(listaId);

        assertEquals(2, resultado.size());
        assertEquals("Tarefa Teste", resultado.getFirst().getNome());
    }

    @Test
    public void deveAtualizarTarefaQuandoIdExistir(){

        TarefaRequestDto requestEditado = TarefaFactory.tarefaRequestDTO()
                .toBuilder()
                .nome("Tarefa Editada")
                .build();
        Tarefa editada = TarefaFactory.tarefaComId()
                .toBuilder()
                .nome("Tarefa Editada")
                .build();
        TarefaResponseDto responseEditado = TarefaFactory.tarefaResponseDTO()
                .toBuilder()
                .nome("Tarefa Editada")
                .build();

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefaComLista));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(editada);
        when(mapper.toResponse(editada)).thenReturn(responseEditado);

        TarefaResponseDto resultado = service.editar(requestEditado,id);

        assertNotNull(resultado.getId());
        assertEquals("Tarefa Editada", resultado.getNome());

    }

    @Test
    public void deveDeletarTarefaQuandoIdExistir(){

        when(tarefaRepository.existsById(id)).thenReturn(true);
        doNothing().when(tarefaRepository).deleteById(id);

        service.deletar(id);

        verify(tarefaRepository).existsById(id);
        verify(tarefaRepository).deleteById(id);
    }


    // -------------------------------
    // TESTES DE EXCEÇÃO (fluxos alternativos)
    // -------------------------------

    @Test
    public void deveLancarExcecaoAoCriarTarefaComListaIdInexistente(){


        when(listaRepository.findById(listaIdInvalido)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(request,listaIdInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Lista não encontrada");

        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    public void deveLancarExcecaoQuandoIdNaoExistir(){

        when(tarefaRepository.findById(idInvalido)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPeloId(idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tarefa não encontrada");

        verify(mapper, never()).toResponse(any(Tarefa.class));
    }

    @Test
    public void deveLancarExcecaoQuandoListarTarefasDeListaInexistente(){

        when(listaRepository.existsById(listaIdInvalido)).thenReturn(false);

        assertThatThrownBy(() -> service.listarTarefasPelaListaId(listaIdInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Lista não encontrada");

        verify(tarefaRepository, never()).findAllByListaId(listaIdInvalido);
        verify(mapper, never()).toListResponse(any());
    }

    @Test
    public void deveLancarExcecaoQuandoAtualizarTarefaComIdInexistente(){

        when(tarefaRepository.findById(idInvalido)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editar(request,idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tarefa não encontrada");

        verify(tarefaRepository, never()).save(any(Tarefa.class));
        verify(mapper, never()).toResponse(any(Tarefa.class));
    }


    @Test
    public void deveLancarExcecaoQuandoDeletarComIdInexistente(){
        when(tarefaRepository.existsById(idInvalido)).thenReturn(false);

        assertThatThrownBy(() -> service.deletar(idInvalido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tarefa não encontrada");

        verify(tarefaRepository, never()).deleteById(idInvalido);
    }
}
