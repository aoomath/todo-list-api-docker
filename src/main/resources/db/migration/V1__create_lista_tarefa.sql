CREATE TABLE lista (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    criacao TIMESTAMP NOT NULL
);

CREATE TABLE tarefa (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    lista_id UUID,
    CONSTRAINT fk_lista FOREIGN KEY(lista_id) REFERENCES lista(id) ON DELETE CASCADE
);
