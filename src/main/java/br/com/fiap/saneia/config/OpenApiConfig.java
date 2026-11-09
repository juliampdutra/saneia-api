package br.com.fiap.saneia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI saneiaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Saneia API")
                        .version("v1")
                        .description("""
                                API REST do Saneia - Global Solution 3SIR (FIAP).
                                Moradores de São Paulo reportam problemas de saneamento
                                (esgoto, vazamento, falta d'água, bueiro entupido) por distrito,
                                e as equipes registram os atendimentos até a resolução.

                                Fluxo sugerido para testar: Distrito -> Morador -> Ocorrência -> Atendimento.

                                Códigos de erro: 400 dados inválidos | 404 recurso inexistente |
                                409 violação de integridade (duplicado ou com vínculos) |
                                422 regra de negócio (ocorrência já encerrada) | 500 erro interno (mensagem neutra).
                                Integrantes: Julia Mendes Pessoa Dutra (RM557645) e Rebeca Berbert Barcelos (RM556683).""")
                        .contact(new Contact()
                                .name("Julia Mendes Pessoa Dutra / Rebeca Berbert Barcelos")
                                .url("https://github.com/juliampdutra/saneia-api")));
    }
}
