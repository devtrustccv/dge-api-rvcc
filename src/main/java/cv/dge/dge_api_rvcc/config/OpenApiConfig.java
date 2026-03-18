package cv.dge.dge_api_rvcc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${api.version}") String apiVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("DGE API RVCC")
                        .version(apiVersion)
                        .description("Documentacao da API DGE RVCC")
                        .contact(new Contact()
                                .name("DGE")
                                .email("suporte@dge.cv")));
    }
}
