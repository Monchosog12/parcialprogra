package co.edu.unbosque.backend.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Swagger en http://localhost:8080/ . Para probar endpoints protegidos: hacer
 * login en /auth/login, copiar el token y pegarlo en el boton "Authorize".
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Backend API", version = "1.0", description = "API con autenticacion JWT"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {
}
