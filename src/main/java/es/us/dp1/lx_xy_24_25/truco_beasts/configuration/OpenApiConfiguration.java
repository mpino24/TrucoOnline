package es.us.dp1.lx_xy_24_25.truco_beasts.configuration;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
  info =@Info(
    title = "APIs de Truco Beast: Bardo en la jungla",
    version = "v1.0",
    contact = @Contact(
      name = "DP1-2024-Group 3", email = "midpie@gmail.com", url = "https://www.youtube.com/@MIDPIE-TRUCO"
    ),
    license = @License(
      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    ),
    
    termsOfService = "${tos.uri}",
    description = "${api.description}"
  )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfiguration {
    
}
