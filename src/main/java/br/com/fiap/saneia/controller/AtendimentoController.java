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

import br.com.fiap.saneia.dto.request.AtendimentoRequest;
import br.com.fiap.saneia.dto.response.AtendimentoResponse;
import br.com.fiap.saneia.exception.ErroResponse;
import br.com.fiap.saneia.service.AtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/atendimentos")
@Tag(name = "Atendimentos", description = "Atendimentos agendados pelas equipes para resolver as ocorrências")
public class AtendimentoController {

    // A Controller conhece apenas a Service (nunca o Repository)
    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar atendimento", description = "Cria um atendimento e retorna 201 com o registro criado e o header Location.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Atendimento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (validação, JSON malformado ou enum inexistente)",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ocorrência informada (ocorrenciaId) não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "422", description = "Ocorrência já está RESOLVIDA ou CANCELADA",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<AtendimentoResponse> criar(@Valid @RequestBody AtendimentoRequest request) {
        AtendimentoResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar atendimentos", description = "Retorna todos os atendimentos cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    })
    public ResponseEntity<List<AtendimentoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar atendimento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atendimento encontrado"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<AtendimentoResponse> buscarPorId(
            @Parameter(description = "ID do atendimento", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar atendimento", description = "Substitui os dados do atendimento informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atendimento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado ou ocorrência informada (ocorrenciaId) não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "422", description = "Nova ocorrência informada já está RESOLVIDA ou CANCELADA",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<AtendimentoResponse> atualizar(
            @Parameter(description = "ID do atendimento", example = "1") @PathVariable Long id,
            @Valid @RequestBody AtendimentoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir atendimento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atendimento excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do atendimento", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
