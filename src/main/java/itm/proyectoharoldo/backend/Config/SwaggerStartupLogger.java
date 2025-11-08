package itm.proyectoharoldo.backend.Config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SwaggerStartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        String port = event.getApplicationContext().getEnvironment().getProperty("server.port", "8080");

        System.out.println("\n------------------------------------------------------------");
        System.out.println("Swagger UI: http://localhost:" + port + "/swagger-ui/index.html");
        System.out.println("OpenAPI JSON: http://localhost:" + port + "/v3/api-docs");
        System.out.println("------------------------------------------------------------\n");
    }

}
