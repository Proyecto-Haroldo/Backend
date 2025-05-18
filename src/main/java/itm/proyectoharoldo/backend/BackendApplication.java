package itm.proyectoharoldo.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		Dotenv env = Dotenv.configure().load();
		SpringApplication.run(BackendApplication.class, args);
	}

}
