package br.com.fiap.saneia.dto.request;

import br.com.fiap.saneia.model.enums.Zona;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar ou atualizar um distrito")
public class DistritoRequest {

    @Schema(description = "Nome do distrito", example = "Sé", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @Schema(description = "Zona da cidade", example = "CENTRO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A zona é obrigatória (NORTE, SUL, LESTE, OESTE ou CENTRO)")
    private Zona zona;

    @Schema(description = "Subprefeitura responsável", example = "Sé", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A subprefeitura é obrigatória")
    @Size(max = 100, message = "A subprefeitura deve ter no máximo 100 caracteres")
    private String subprefeitura;

    @Schema(description = "Código do distrito no IBGE", example = "355030877")
    @Size(max = 20, message = "O código IBGE deve ter no máximo 20 caracteres")
    private String codigoIbge;

    @Schema(description = "População estimada", example = "23651")
    @PositiveOrZero(message = "A população estimada não pode ser negativa")
    private Integer populacaoEstimada;

    @Schema(description = "Área em km²", example = "2.1")
    @Positive(message = "A área deve ser maior que zero")
    private Double areaKm2;

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