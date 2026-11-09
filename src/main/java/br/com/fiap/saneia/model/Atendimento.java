package br.com.fiap.saneia.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.saneia.model.enums.StatusAtendimento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipe_responsavel", length = 100, nullable = false)
    private String equipeResponsavel;

    @Column(name = "data_agendada", nullable = false)
    private LocalDateTime dataAgendada;

    @Column(name = "descricao_servico", length = 500)
    private String descricaoServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusAtendimento status;

    @Column(name = "custo_estimado", precision = 12, scale = 2)
    private BigDecimal custoEstimado;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ocorrencia_id", nullable = false)
    private Ocorrencia ocorrencia;

    public Atendimento() {
    }

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

    public Ocorrencia getOcorrencia() { return ocorrencia; }
    public void setOcorrencia(Ocorrencia ocorrencia) { this.ocorrencia = ocorrencia; }
}
