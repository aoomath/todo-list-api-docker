package com.example.Teste.ToDo.Docker.factory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageFactory {

    public static <T> Page<T> createPage(List<T> content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(content, pageable, content.size());
    }

    public static <T> Page<T> listaToPage(List<T> listas) {

        return createPage(listas, 0, listas.size());
    }


}
