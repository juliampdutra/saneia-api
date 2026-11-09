package br.com.fiap.saneia.dto.request;

import br.com.fiap.saneia.model.enums.Prioridade;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import br.com.fiap.saneia.model.enums.TipoOcorrencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registrar ou atualizar uma ocorrência de saneamento")
public class OcorrenciaRequest {

    @Schema(description = "Título curto", example = "Vazamento na calçada", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo;

    @Schema(description = "Descrição do problema", example = "Água limpa escorrendo pela calçada há 2 dias", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @Schema(description = "Tipo do problema", example = "VAZAMENTO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O tipo é obrigatório (ESGOTO_A_CEU_ABERTO, VAZAMENTO, FALTA_DE_AGUA, BUEIRO_ENTUPIDO ou OUTRO)")
    private TipoOcorrencia tipo;

    @Schema(description = "Prioridade", example = "ALTA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A prioridade é obrigatória (BAIXA, MEDIA, ALTA ou URGENTE)")
    private Prioridade prioridade;

    @Schema(description = "Endereço/ponto de referência do problema", example = "Rua Direita, 250 - em frente à padaria", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O endereço de referência é obrigatório")
    @Size(max = 200, message = "O endereço de referência deve ter no máximo 200 caracteres")
    private String enderecoReferencia;

    @Schema(description = "Status da ocorrência. Ignorado na criação (sempre começa ABERTA); no PUT permite alterar (ex.: CANCELADA).", example = "ABERTA")
    private StatusOcorrencia status;

    @Schema(description = "ID do morador que reportou", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O moradorId é obrigatório")
    @Positive(message = "O moradorId deve ser positivo")
    private Long moradorId;

    @Schema(description = "ID do distrito onde está o problema", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O distritoId é obrigatório")
    @Positive(message = "O distritoId deve ser positivo")
    private Long distritoId;

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

    public Long getMoradorId() { return moradorId; }
    public void setMoradorId(Long moradorId) { this.moradorId = moradorId; }

    public Long getDistritoId() { return distritoId; }
    public void setDistritoId(Long distritoId) { this.distritoId = distritoId; }
}
