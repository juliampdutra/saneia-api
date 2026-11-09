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

import br.com.fiap.saneia.dto.request.MoradorRequest;
import br.com.fiap.saneia.dto.response.MoradorResponse;
import br.com.fiap.saneia.exception.ErroResponse;
import br.com.fiap.saneia.service.MoradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/moradores")
@Tag(name = "Moradores", description = "Moradores que reportam problemas de saneamento")
public class MoradorController {

    // A Controller conhece apenas a Service (nunca o Repository)
    private final MoradorService service;

    public MoradorController(MoradorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar morador", description = "Cria um morador e retorna 201 com o registro criado e o header Location.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Morador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (validação, JSON malformado ou enum inexistente)",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Distrito informado (distritoId) não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MoradorResponse> criar(@Valid @RequestBody MoradorRequest request) {
        MoradorResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar moradores", description = "Retorna todos os moradores cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    })
    public ResponseEntity<List<MoradorResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar morador por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador encontrado"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MoradorResponse> buscarPorId(
            @Parameter(description = "ID do morador", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar morador", description = "Substitui os dados do morador informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado ou distrito informado (distritoId) não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<MoradorResponse> atualizar(
            @Parameter(description = "ID do morador", example = "1") @PathVariable Long id,
            @Valid @RequestBody MoradorRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir morador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Morador excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Morador não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Morador possui ocorrências vinculadas",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do morador", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
