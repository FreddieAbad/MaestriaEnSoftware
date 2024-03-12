package ec.jtux.demoIntegration.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class PrintService {

    Logger logger = LoggerFactory.getLogger(PrintService.class);

    @ServiceActivator(inputChannel = "echoChannel")
    public void print(String message){
        logger.info(message);
    }

    @ServiceActivator(inputChannel = "reverseEchoChannel")
    public void reverse(String message){
        logger.info(new StringBuilder(message).reverse().toString());
    }
    
}
