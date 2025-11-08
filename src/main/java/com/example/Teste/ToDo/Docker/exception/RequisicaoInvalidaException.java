package com.example.ToDoList.exception;

public class RequisicaoInvalidaException extends RuntimeException{
    public RequisicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
