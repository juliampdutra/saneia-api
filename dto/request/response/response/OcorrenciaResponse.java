package br.com.fiap.saneia.dto.response;

import java.time.LocalDateTime;

import br.com.fiap.saneia.model.enums.Prioridade;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import br.com.fiap.saneia.model.enums.TipoOcorrencia;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ocorrência retornada pela API")
public class OcorrenciaResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private TipoOcorrencia tipo;
    private Prioridade prioridade;
    private String enderecoReferencia;
    private StatusOcorrencia status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataResolucao;
    private Long moradorId;
    private String moradorNome;
    private Long distritoId;
    private String distritoNome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TipoOcorrencia getTipo() { return tipo; }
    public void setTipo(TipoOcorrencia tipo) { this.tipo = tipo; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public String getEnderecoReferencia() { return enderecoReferencia; }
    public void setEnderecoReferencia(String enderecoReferencia) { this.enderecoReferencia = enderecoReferencia; }

    public StatusOcorrencia getStatus() { return status; }
    public void setStatus(StatusOcorrencia status) { this.status = status; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDateTime getDataResolucao() { return dataResolucao; }
    public void setDataResolucao(LocalDateTime dataResolucao) { this.dataResolucao = dataResolucao; }

    public Long getMoradorId() { return moradorId; }
    public void setMoradorId(Long moradorId) { this.moradorId = moradorId; }

    public String getMoradorNome() { return moradorNome; }
    public void setMoradorNome(String moradorNome) { this.moradorNome = moradorNome; }

    public Long getDistritoId() { return distritoId; }
    public void setDistritoId(Long distritoId) { this.distritoId = distritoId; }

    public String getDistritoNome() { return distritoNome; }
    public void setDistritoNome(String distritoNome) { this.distritoNome = distritoNome; }
}
