package br.com.fiap.saneia.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.saneia.model.enums.StatusAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para agendar ou atualizar um atendimento de uma ocorrência")
public class AtendimentoRequest {

    @Schema(description = "Equipe responsável", example = "Equipe Sabesp Centro 03", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A equipe responsável é obrigatória")
    @Size(max = 100, message = "A equipe responsável deve ter no máximo 100 caracteres")
    private String equipeResponsavel;

    @Schema(description = "Data e hora agendadas (yyyy-MM-ddTHH:mm:ss)", example = "2026-11-20T09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A data agendada é obrigatória")
    private LocalDateTime dataAgendada;

    @Schema(description = "Descrição do serviço", example = "Troca do trecho da tubulação danificada")
    @Size(max = 500, message = "A descrição do serviço deve ter no máximo 500 caracteres")
    private String descricaoServico;

    @Schema(description = "Status do atendimento. Se não informado, começa AGENDADO. Ao enviar CONCLUIDO, a ocorrência vira RESOLVIDA.", example = "AGENDADO")
    private StatusAtendimento status;

    @Schema(description = "Custo estimado em reais", example = "350.00")
    @PositiveOrZero(message = "O custo estimado não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "O custo estimado deve ter no máximo 2 casas decimais")
    private BigDecimal custoEstimado;

    @Schema(description = "ID da ocorrência atendida", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O ocorrenciaId é obrigatório")
    @Positive(message = "O ocorrenciaId deve ser positivo")
    private Long ocorrenciaId;

    public String getEquipeResponsavel() { return equipeResponsavel; }
    public void setEquipeResponsavel(String equipeResponsavel) { this.equipeResponsavel = equipeResponsavel; }

    public LocalDateTime getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDateTime dataAgendada) { this.dataAgendada = dataAgendada; }

    public String getDescricaoServico() { return descricaoServico; }
    public void setDescricaoServico(String descricaoServico) { this.descricaoServico = descricaoServico; }

    public StatusAtendimento getStatus() { return status; }
    public void setStatus(StatusAtendimento status) { this.status = status; }

    public BigDecimal getCustoEstimado() { return custoEstimado; }
    public void setCustoEstimado(BigDecimal custoEstimado) { this.custoEstimado = custoEstimado; }

    public Long getOcorrenciaId() { return ocorrenciaId; }
    public void setOcorrenciaId(Long ocorrenciaId) { this.ocorrenciaId = ocorrenciaId; }
}
