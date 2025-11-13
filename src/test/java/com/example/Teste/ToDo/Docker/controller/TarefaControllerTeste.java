package com.example.Teste.ToDo.Docker.controller;

import com.example.Teste.ToDo.Docker.dto.tarefa.TarefaRequestDto;
import com.example.Teste.ToDo.Docker.factory.ListaFactory;
import com.example.Teste.ToDo.Docker.factory.TarefaFactory;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TarefaControllerTeste {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @LocalServerPort
    private int port;

    private String listaId;
    private String tarefaId;
    private UUID idInvalido;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        listaId = given()
                .contentType(ContentType.JSON)
                .body(ListaFactory.listaRequestDTO())
                .when()
                .post("/listas")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        idInvalido=UUID.randomUUID();

        tarefaId = given()
                .pathParam("listaId", UUID.fromString(listaId))
                .contentType(ContentType.JSON)
                .body(TarefaFactory.tarefaRequestDTO())
                .when()
                .post("/tarefas/listas/{listaId}")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        }


    // -------------------------------
    // TESTES POSITIVOS (fluxo feliz)
    // -------------------------------

    @Test
    public void deveSalvarTarefaComSucesso(){

        given()
                .pathParam("listaId", UUID.fromString(listaId))
                .contentType(ContentType.JSON)
                .body(TarefaFactory.tarefaRequestDTO())
                .when()
                .post("/tarefas/listas/{listaId}")
                .then()
                .statusCode(201)
                .body("nome", equalTo("Tarefa Teste"));
    }

    @Test
    public void deveBuscarTarefaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(tarefaId))
                .when()
                .get("/tarefas/{id}")
                .then()
                .statusCode(200)
                .body("nome", equalTo("Tarefa Teste"));
    }


    @Test
    public void deveRetornarListaDeTarefaComIdListaValido(){

        given()
                .pathParam("listaId", UUID.fromString(listaId))
                .when()
                .get("/tarefas/listas/{listaId}")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].nome", equalTo("Tarefa Teste"));

    }

    @Test
    public void deveEditarTarefaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(tarefaId))
                .contentType(ContentType.JSON)
                .body("{\"nome\": \"Tarefa Atualizada\"}")
                .when()
                .put("/tarefas/{id}")
                .then()
                .statusCode(200)
                .body("nome", equalTo("Tarefa Atualizada"));
    }

    @Test
    public void deveDeletarTarefaComIdValido(){

        given()
                .pathParam("id", UUID.fromString(tarefaId))
                .when()
                .delete("/tarefas/{id}")
                .then()
                .statusCode(204);

        // Verifica que realmente foi deletado

        given()
                .pathParam("id", UUID.fromString(tarefaId))
                .when()
                .get("/tarefas/{id}")
                .then()
                .statusCode(404);
    }

    // -------------------------------
    // TESTES DE EXCEÇÃO (fluxos alternativos)
    // -------------------------------

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    public void deveLancarExcecaoAoSalvarTarefaSemNome(String nomeInvalido){
        TarefaRequestDto request = TarefaFactory.tarefaRequestDTO().toBuilder().nome(nomeInvalido).build();

        given()
                .pathParam("listaId", UUID.fromString(listaId))
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/tarefas/listas/{listaId}")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Erro de validação"))
                .body("mensagem", equalTo("Campos inválidos na requisição"))
                .body("detalhes[0]", equalTo("Campo 'nome': O nome da tarefa é obrigatório"));;


    }

    @Test
    public void deveLancarExcecaoAoSalvarTarefaComIdListaInvalido(){
        given()
                .pathParam("listaId", idInvalido)
                .contentType(ContentType.JSON)
                .body(TarefaFactory.tarefaRequestDTO())
                .when()
                .post("/tarefas/listas/{listaId}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Lista não encontrada"));
    }

    @Test
    public void deveLancarExcecaoAoBuscarTarefaComIdInvalido(){

        given()
                .pathParam("id", idInvalido)
                .when()
                .get("/tarefas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Tarefa não encontrada"));
    }

    @Test
    public void deveLancarExcecaoAoBuscarListaTarefaComIdListaInvalido(){

        given()
                .pathParam("listaId", idInvalido)
                .when()
                .get("/tarefas/listas/{listaId}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Lista não encontrada"));

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    public void deveLancarExcecaoAoEditarTarefaSemNome(String nomeInvalido){
        TarefaRequestDto request = TarefaFactory.tarefaRequestDTO().toBuilder().nome(nomeInvalido).build();

        given()
                .pathParam("id", UUID.fromString(tarefaId))
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/tarefas/{id}")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Erro de validação"))
                .body("mensagem", equalTo("Campos inválidos na requisição"))
                .body("detalhes[0]", equalTo("Campo 'nome': O nome da tarefa é obrigatório"));;

    }
    @Test
    public void deveLancarExcecaoAoEditarTarefaComIdListaInvalido(){

        given()
                .pathParam("id", idInvalido)
                .contentType(ContentType.JSON)
                .body("{\"nome\": \"Tarefa Atualizada\"}")
                .when()
                .put("/tarefas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Tarefa não encontrada"));
    }

    @Test
    public void deveLancarExcecaoAoDeletarTarefaComIdInvalido(){

        given()
                .pathParam("id", idInvalido)
                .when()
                .delete("/tarefas/{id}")
                .then()
                .statusCode(404)
                .body("erro", equalTo("Recurso não encontrado"))
                .body("mensagem", equalTo("Tarefa não encontrada"));

    }


}
