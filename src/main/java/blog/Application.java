package blog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {
		@Server(url = "https://personal-blogging-platform-api.up.railway.app/", description = "Personal Blogging Platform API"),
		@Server(url = "http://localhost:8080/", description = "The API is not online. It must run on your local server")
})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
