package ec.jtux.demoIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ec.jtux.demoIntegration.endpoint.CustomGateway;

@SpringBootApplication
@Configuration
public class DemoIntegrationApplication {

	@Autowired
	private CustomGateway gateway;

	public static void main(String[] args) {
		SpringApplication.run(DemoIntegrationApplication.class, args);
	}

	@Bean
	public CommandLineRunner inicar(){
		return (args) -> {
			gateway.print("Hola mundo");
			gateway.reverse("Hola mundo");
		};
	}

}
