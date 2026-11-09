package br.com.fiap.saneia.model;

import br.com.fiap.saneia.model.enums.Zona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "distritos")
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "zona", length = 10, nullable = false)
    private Zona zona;

    @Column(name = "subprefeitura", length = 100, nullable = false)
    private String subprefeitura;

    @Column(name = "codigo_ibge", length = 20)
    private String codigoIbge;

    @Column(name = "populacao_estimada")
    private Integer populacaoEstimada;

    @Column(name = "area_km2")
    private Double areaKm2;

    public Distrito() {
    }

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
