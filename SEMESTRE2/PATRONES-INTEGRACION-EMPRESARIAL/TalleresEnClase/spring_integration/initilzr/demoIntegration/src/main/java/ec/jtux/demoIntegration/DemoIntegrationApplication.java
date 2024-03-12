package ec.jtux.demoIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ec.jtux.demoIntegration.endpoint.CustomGateway;

@SpringBootApplication
public class DemoIntegrationApplication {
@Autowired
	private CustomGateway gateway;
	public static void main(String[] args) {
		SpringApplication.run(DemoIntegrationApplication.class, args);
	}
	@Bean
	public CommandLineRunner iniciar(){
		return (args) -> {
			gateway.print("Patrones de Integracion Empresarial");
			gateway.reverse("Patrones de Integracion Empresarial");
		};
	}
}
