package br.com.fiap.saneia.dto.response;

import br.com.fiap.saneia.model.enums.Zona;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Distrito retornado pela API")
public class DistritoResponse {

    private Long id;
    private String nome;
    private Zona zona;
    private String subprefeitura;
    private String codigoIbge;
    private Integer populacaoEstimada;
    private Double areaKm2;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

    public String getSubprefeitura() { return subprefeitura; }
    public void setSubprefeitura(String subprefeitura) { this.subprefeitura = subprefeitura; }

    public String getCodigoIbge() { return codigoIbge; }
    public void setCodigoIbge(String codigoIbge) { this.codigoIbge = codigoIbge; }

    public Integer getPopulacaoEstimada() { return populacaoEstimada; }
    public void setPopulacaoEstimada(Integer populacaoEstimada) { this.populacaoEstimada = populacaoEstimada; }

    public Double getAreaKm2() { return areaKm2; }
    public void setAreaKm2(Double areaKm2) { this.areaKm2 = areaKm2; }
}
