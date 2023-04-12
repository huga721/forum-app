package huberts.spring.forumapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "Forum REST API app"),
                    security = @SecurityRequirement(name = "JWT"))
@SecurityScheme(name = "JWT",
        description = "JWT authentication with bearer token",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP
)
public class OpenApiConfig {}