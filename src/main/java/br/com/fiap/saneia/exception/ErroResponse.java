package br.com.fiap.saneia.exception;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Corpo padrão de erro da API")
public record ErroResponse(
        @Schema(example = "2026-11-20T10:15:30") LocalDateTime timestamp,
        @Schema(example = "400") int status,
        @Schema(example = "Bad Request") String erro,
        @Schema(example = "Dados inválidos. Verifique os campos informados.") String mensagem,
        @Schema(example = "/api/v1/moradores") String path,
        List<CampoInvalido> campos) {
}
