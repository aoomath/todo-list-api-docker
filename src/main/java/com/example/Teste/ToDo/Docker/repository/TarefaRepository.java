package com.example.Teste.ToDo.Docker.repository;

import com.example.Teste.ToDo.Docker.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {
    List<Tarefa> findAllByListaId(UUID listaId);
}
