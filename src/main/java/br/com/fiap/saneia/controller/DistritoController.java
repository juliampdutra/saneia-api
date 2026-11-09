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

import br.com.fiap.saneia.dto.request.DistritoRequest;
import br.com.fiap.saneia.dto.response.DistritoResponse;
import br.com.fiap.saneia.exception.ErroResponse;
import br.com.fiap.saneia.service.DistritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${api.version}/distritos")
@Tag(name = "Distritos", description = "Distritos de São Paulo onde moradores vivem e onde os problemas de saneamento acontecem")
public class DistritoController {

    // A Controller conhece apenas a Service (nunca o Repository)
    private final DistritoService service;

    public DistritoController(DistritoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar distrito", description = "Cria um distrito e retorna 201 com o registro criado e o header Location.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Distrito criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (validação, JSON malformado ou enum inexistente)",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe distrito com esse nome",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DistritoResponse> criar(@Valid @RequestBody DistritoRequest request) {
        DistritoResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar distritos", description = "Retorna todos os distritos cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    })
    public ResponseEntity<List<DistritoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar distrito por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Distrito encontrado"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Distrito não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DistritoResponse> buscarPorId(
            @Parameter(description = "ID do distrito", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar distrito", description = "Substitui os dados do distrito informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Distrito atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Distrito não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe distrito com esse nome",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DistritoResponse> atualizar(
            @Parameter(description = "ID do distrito", example = "1") @PathVariable Long id,
            @Valid @RequestBody DistritoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir distrito")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Distrito excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID em formato inválido",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Distrito não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Distrito possui moradores ou ocorrências vinculados",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do distrito", example = "1") @PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
