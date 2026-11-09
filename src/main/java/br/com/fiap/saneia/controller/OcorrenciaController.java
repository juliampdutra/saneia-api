package br.com.fiap.saneia.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.fiap.saneia.dto.request.OcorrenciaRequest;
import br.com.fiap.saneia.dto.response.OcorrenciaResponse;
import br.com.fiap.saneia.exception.ErroResponse;
import br.com.fiap.saneia.service.OcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/ocorrencias")
@Tag(name = "Ocorrências", description = "Problemas de saneamento reportados (esgoto, vazamento, falta d'água, bueiro entupido)")
public class OcorrenciaController {

    // A Controller conhece apenas a Service (nunca o Repository)
    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar ocorrência", description = "Cria uma ocorrência e retorna 201 com o registro criado e o header Location.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ocorrência criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (validação, JSON malformado ou enum inexistente)",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador ou distrito informado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<OcorrenciaResponse> criar(@Valid @RequestBody OcorrenciaRequest request) {
        OcorrenciaResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar ocorrências", description = "Retorna todas as ocorrências cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    })
    public ResponseEntity<List<OcorrenciaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ocorrência por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ocorrência encontrada"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<OcorrenciaResponse> buscarPorId(
            @Parameter(description = "ID da ocorrência", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ocorrência", description = "Substitui os dados da ocorrência informada pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ocorrência atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ocorrência, morador ou distrito não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<OcorrenciaResponse> atualizar(
            @Parameter(description = "ID da ocorrência", example = "1") @PathVariable Long id,
            @Valid @RequestBody OcorrenciaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir ocorrência")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ocorrência excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ocorrência possui atendimentos vinculados",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da ocorrência", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
