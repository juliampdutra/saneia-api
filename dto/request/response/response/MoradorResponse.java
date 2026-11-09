package br.com.fiap.saneia.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Morador retornado pela API")
public class MoradorResponse {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cep;
    private String logradouro;
    private LocalDateTime dataCadastro;
    private Long distritoId;
    private String distritoNome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public Long getDistritoId() { return distritoId; }
    public void setDistritoId(Long distritoId) { this.distritoId = distritoId; }

    public String getDistritoNome() { return distritoNome; }
    public void setDistritoNome(String distritoNome) { this.distritoNome = distritoNome; }
}