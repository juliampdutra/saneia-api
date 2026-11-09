package br.com.fiap.saneia.model;

import java.time.LocalDateTime;

import br.com.fiap.saneia.model.enums.Prioridade;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import br.com.fiap.saneia.model.enums.TipoOcorrencia;
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
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "descricao", length = 1000, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30, nullable = false)
    private TipoOcorrencia tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", length = 10, nullable = false)
    private Prioridade prioridade;

    @Column(name = "endereco_referencia", length = 200, nullable = false)
    private String enderecoReferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusOcorrencia status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "morador_id", nullable = false)
    private Morador morador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "distrito_id", nullable = false)
    private Distrito distrito;

    public Ocorrencia() {
    }

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

    public Morador getMorador() { return morador; }
    public void setMorador(Morador morador) { this.morador = morador; }

    public Distrito getDistrito() { return distrito; }
    public void setDistrito(Distrito distrito) { this.distrito = distrito; }
}
