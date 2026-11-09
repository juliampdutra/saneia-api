package br.com.fiap.saneia.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar ou atualizar um morador")
public class MoradorRequest {

    @Schema(description = "Nome completo", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @Schema(description = "E-mail (único)", example = "maria.silva@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @Schema(description = "Telefone com DDD", example = "11987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 10, max = 20, message = "O telefone deve ter entre 10 e 20 caracteres")
    private String telefone;

    @Schema(description = "CEP", example = "01001-000")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve estar no formato 00000-000")
    private String cep;

    @Schema(description = "Logradouro e número", example = "Praça da Sé, 100")
    @Size(max = 200, message = "O logradouro deve ter no máximo 200 caracteres")
    private String logradouro;

    @Schema(description = "ID do distrito onde o morador reside", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O distritoId é obrigatório")
    @Positive(message = "O distritoId deve ser positivo")
    private Long distritoId;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public Long getDistritoId() { return distritoId; }
    public void setDistritoId(Long distritoId) { this.distritoId = distritoId; }
}
