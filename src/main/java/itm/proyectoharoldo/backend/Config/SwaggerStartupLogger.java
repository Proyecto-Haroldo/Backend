package itm.proyectoharoldo.backend.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SwaggerStartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger logger = LoggerFactory.getLogger(SwaggerStartupLogger.class);

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");

        logger.info("\n------------------------------------------------------------\n" + 
            "Swagger UI: http://localhost:" + port + "/swagger-ui/index.html" + "\n" +
            "OpenAPI JSON: http://localhost:" + port + "/v3/api-docs" + "\n" +
            "------------------------------------------------------------\n"
        );
    }

}
