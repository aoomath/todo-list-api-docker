package com.example.Teste.ToDo.Docker;

import org.springframework.boot.SpringApplication;

public class TestToDoListApplication {

	public static void main(String[] args) {
		SpringApplication.from(ToDoListApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
