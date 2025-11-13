package com.example.Teste.ToDo.Docker.controller;

import com.example.Teste.ToDo.Docker.dto.lista.ListaRequestDto;
import com.example.Teste.ToDo.Docker.factory.ListaFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ListaControllerTeste {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");


    @LocalServerPort
    private int port;

    private String idCriado;
    private UUID idInvalido;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Cria uma lista antes de cada teste
        idCriado = given()
                        .contentType(ContentType.JSON)
                        .body(ListaFactory.listaRequestDTO())
                        .when()
                        .post("/listas")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        idInvalido=UUID.randomUUID();
    }


    // -------------------------------
    // TESTES POSITIVOS (fluxo feliz)
    // -------------------------------


    @Test
    void deveSalvarListaComSucesso() {
        given()
                .contentType(ContentType.JSON)
                .body(ListaFactory.listaRequestDTO())
                .when()
                .post("/listas")
                .then()
                .statusCode(201)
                .body("titulo", equalTo("Titulo teste"));
    }

    @Test
    public void deveBuscarListaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(idCriado))
                .when()
                .get("/listas/{id}")
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Titulo teste"));
    }
    @Test
    public void deveBuscarPaginaDeListas(){

        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/listas")
                .then()
                .statusCode(200)
                .body("content.size()", greaterThan(0))
                .body("content[0].titulo", equalTo("Titulo teste"));

    }
    @Test
    public void deveFiltrarListasPorTituloEPeriodo() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("titulo", "teste")
                .queryParam("dataInicio", "01/01/2024")
                .queryParam("dataFim", "12/12/2025")
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get("/listas/filtrar")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content[0].titulo", containsStringIgnoringCase("teste"))
                .body("content[0].id", notNullValue())
                .body("page.size", equalTo(5));
    }

    @Test
    public void deveEditarListaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(idCriado))
                .contentType(ContentType.JSON)
                .body("{\"titulo\": \"Lista Atualizada\"}")
                .when()
                .put("/listas/{id}")
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Lista Atualizada"));
    }

    @Test
    public void deveDeletarListaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(idCriado))
                .when()
                .delete("/listas/{id}")
                .then()
                .statusCode(204);

        // Verifica que realmente foi deletado

        given()
                .pathParam("id", UUID.fromString(idCriado))
                .when()
                .get("/listas/{id}")
                .then()
                .statusCode(404);
    }


    // -------------------------------
    // TESTES DE EXCEÇÃO (fluxos alternativos)
    // -------------------------------

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    public void deveLancarExcecaoAoSalvarListaSemTitulo(String tituloInvalido){

        ListaRequestDto request = ListaFactory.listaRequestDTO().toBuilder().titulo(tituloInvalido).build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/listas")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Erro de validação"))
                .body("mensagem", equalTo("Campos inválidos na requisição"))
                .body("detalhes[0]", equalTo("Campo 'titulo': O título da lista é obrigatório"));;
    }

    @Test
    public void deveLancarExcecaoAoBuscarListaComIdInvalido(){

        given()
                .pathParam("id", idInvalido)
                .when()
                .get("/listas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Lista não encontrada"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    public void deveLancarExcecaoAoAtualizarComDtoInvalidos(String tituloInvalido){

        ListaRequestDto request = ListaFactory.listaRequestDTO().toBuilder().titulo(tituloInvalido).build();

        given()
                .pathParam("id", UUID.fromString(idCriado))
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/listas/{id}")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Erro de validação"))
                .body("mensagem", equalTo("Campos inválidos na requisição"))
                .body("detalhes[0]", equalTo("Campo 'titulo': O título da lista é obrigatório"));;


    }


    @Test
    public void deveLancarExcecaoAoAtualizarComIdInvalidos(){

        given()
                .pathParam("id", idInvalido)
                .contentType(ContentType.JSON)
                .body("{\"titulo\": \"Lista Atualizada\"}")
                .when()
                .put("/listas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Lista não encontrada"));


    }

    @Test
    public void deveLancarExcecaoAoDeletarListaComIdInvalido(){

        given()
                .pathParam("id", idInvalido)
                .when()
                .delete("/listas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Lista não encontrada"));

    }

}
