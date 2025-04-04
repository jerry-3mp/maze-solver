package io.jistud.mazesolver.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/** Configuration class for Swagger OpenAPI documentation. */
@Configuration
public class SwaggerConfig {

  /**
   * Configures the OpenAPI documentation with application information.
   *
   * @return the OpenAPI configuration
   */
  @Bean
  public OpenAPI mazeSolverOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Maze Solver API")
                .description("API for generating and solving mazes")
                .version("v1.0")
                .contact(new Contact().name("Maze Solver API Team").email("support@example.com")));
  }
}
