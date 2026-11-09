package br.com.fiap.saneia.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Campo que falhou na validação")
public record CampoInvalido(
        @Schema(example = "email") String campo,
        @Schema(example = "E-mail inválido") String mensagem) {
}
