package com.example.Teste.ToDo.Docker.repository;

import com.example.Teste.ToDo.Docker.model.Lista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ListaRepository extends JpaRepository<Lista, UUID>, JpaSpecificationExecutor<Lista> {
}
