package br.com.fiap.saneia.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.saneia.dto.response.AtendimentoResponse;
import br.com.fiap.saneia.dto.response.MoradorResponse;
import br.com.fiap.saneia.dto.response.OcorrenciaResponse;
import br.com.fiap.saneia.model.Atendimento;
import br.com.fiap.saneia.model.Morador;
import br.com.fiap.saneia.model.Ocorrencia;

@Configuration
public class ModelMapperConfig {

    /**
     * ModelMapper único da aplicação, injetado nas Services.
     *
     * STRICT: mapeia automaticamente as propriedades de mesmo nome
     * (ex.: nome -> nome, status -> status), sem mapeamentos ambíguos.
     *
     * Os campos dos Response DTOs que vêm dos relacionamentos
     * (ex.: distritoId, distritoNome, moradorNome, ocorrenciaTitulo)
     * são preenchidos por um post-converter em cada TypeMap.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Morador -> MoradorResponse
        modelMapper.typeMap(Morador.class, MoradorResponse.class)
                .setPostConverter(context -> {
                    Morador origem = context.getSource();
                    MoradorResponse destino = context.getDestination();
                    if (origem.getDistrito() != null) {
                        destino.setDistritoId(origem.getDistrito().getId());
                        destino.setDistritoNome(origem.getDistrito().getNome());
                    }
                    return destino;
                });

        // Ocorrencia -> OcorrenciaResponse
        modelMapper.typeMap(Ocorrencia.class, OcorrenciaResponse.class)
                .setPostConverter(context -> {
                    Ocorrencia origem = context.getSource();
                    OcorrenciaResponse destino = context.getDestination();
                    if (origem.getMorador() != null) {
                        destino.setMoradorId(origem.getMorador().getId());
                        destino.setMoradorNome(origem.getMorador().getNome());
                    }
                    if (origem.getDistrito() != null) {
                        destino.setDistritoId(origem.getDistrito().getId());
                        destino.setDistritoNome(origem.getDistrito().getNome());
                    }
                    return destino;
                });

        // Atendimento -> AtendimentoResponse
        modelMapper.typeMap(Atendimento.class, AtendimentoResponse.class)
                .setPostConverter(context -> {
                    Atendimento origem = context.getSource();
                    AtendimentoResponse destino = context.getDestination();
                    if (origem.getOcorrencia() != null) {
                        destino.setOcorrenciaId(origem.getOcorrencia().getId());
                        destino.setOcorrenciaTitulo(origem.getOcorrencia().getTitulo());
                        destino.setOcorrenciaStatus(origem.getOcorrencia().getStatus());
                    }
                    return destino;
                });

        return modelMapper;
    }
}

