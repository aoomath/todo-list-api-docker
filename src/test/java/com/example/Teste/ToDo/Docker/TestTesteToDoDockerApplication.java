package com.example.Teste.ToDo.Docker;

import org.springframework.boot.SpringApplication;

public class TestTesteToDoDockerApplication {

	public static void main(String[] args) {
		SpringApplication.from(TesteToDoDockerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
