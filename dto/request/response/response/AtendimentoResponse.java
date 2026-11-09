package br.com.fiap.saneia.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.saneia.model.enums.StatusAtendimento;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Atendimento retornado pela API")
public class AtendimentoResponse {

    private Long id;
    private String equipeResponsavel;
    private LocalDateTime dataAgendada;
    private String descricaoServico;
    private StatusAtendimento status;
    private BigDecimal custoEstimado;
    private LocalDateTime dataConclusao;
    private Long ocorrenciaId;
    private String ocorrenciaTitulo;
    private StatusOcorrencia ocorrenciaStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public Long getOcorrenciaId() { return ocorrenciaId; }
    public void setOcorrenciaId(Long ocorrenciaId) { this.ocorrenciaId = ocorrenciaId; }

    public String getOcorrenciaTitulo() { return ocorrenciaTitulo; }
    public void setOcorrenciaTitulo(String ocorrenciaTitulo) { this.ocorrenciaTitulo = ocorrenciaTitulo; }

    public StatusOcorrencia getOcorrenciaStatus() { return ocorrenciaStatus; }
    public void setOcorrenciaStatus(StatusOcorrencia ocorrenciaStatus) { this.ocorrenciaStatus = ocorrenciaStatus; }
}