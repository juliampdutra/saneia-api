package br.com.fiap.saneia.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /**
     * ModelMapper único da aplicação, injetado nas Services.
     * STRICT: só mapeia quando o nome bate exatamente
     * (ex.: distritoNome <- distrito.nome), evitando ambiguidade
     * como ocorrencia.distrito.id x ocorrencia.morador.distrito.id.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
