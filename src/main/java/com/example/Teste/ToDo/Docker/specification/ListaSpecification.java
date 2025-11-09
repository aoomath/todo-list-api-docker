package com.example.Teste.ToDo.Docker.specification;

import com.example.Teste.ToDo.Docker.dto.lista.ListaFilter;
import com.example.Teste.ToDo.Docker.model.Lista;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;


public class ListaSpecification {

    public static Specification<Lista> comFiltros(ListaFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTitulo() != null && !filter.getTitulo().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("titulo")), "%" + filter.getTitulo().toLowerCase() + "%")
                );
            }

            if (filter.getDataInicio() != null && filter.getDataFim() != null) {
                predicates.add(cb.between(root.get("criacao"), filter.getDataInicio().atStartOfDay(), filter.getDataFim().atTime(23, 59, 59)));
            } else if (filter.getDataInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("criacao"), filter.getDataInicio().atStartOfDay()));
            } else if (filter.getDataFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("criacao"), filter.getDataFim().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
