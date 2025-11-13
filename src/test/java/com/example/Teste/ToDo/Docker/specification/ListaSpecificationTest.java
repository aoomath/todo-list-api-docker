package com.example.Teste.ToDo.Docker.specification;

import com.example.Teste.ToDo.Docker.dto.lista.ListaFilter;
import com.example.Teste.ToDo.Docker.model.Lista;
import com.example.Teste.ToDo.Docker.repository.ListaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
public class ListaSpecificationTest {

    @Autowired
    private ListaRepository repository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18-alpine");

    @Test
    void deveFiltrarPorTitulo() {
        repository.save(new Lista(null, "Tarefa A", LocalDateTime.now(),null));
        repository.save(new Lista(null, "Outra coisa", LocalDateTime.now(), null));

        ListaFilter filtro = new ListaFilter("Tarefa", null, null);

        Specification<Lista> spec = ListaSpecification.comFiltros(filtro);
        List<Lista> resultado = repository.findAll(spec);

        assertEquals(1, resultado.size());
        assertEquals("Tarefa A", resultado.getFirst().getTitulo());
    }
}
