package br.com.fiap.saneia.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import br.com.fiap.saneia.config.DataHora;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Bean Validation (@Valid) falhou: lista os campos inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex,
                                                        HttpServletRequest request) {
        List<CampoInvalido> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new CampoInvalido(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return montar(HttpStatus.BAD_REQUEST,
                "Dados inválidos. Verifique os campos informados.", request, campos);
    }

    // 400 - JSON malformado, tipo errado ou valor de enum inexistente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleJsonInvalido(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique se o JSON está bem formado, se os tipos "
                + "estão corretos (datas no formato yyyy-MM-ddTHH:mm:ss) e se os valores de enum existem.",
                request, List.of());
    }

    // 400 - Parâmetro de rota com tipo errado (ex.: /api/v1/distritos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTipoParametro(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String mensagem = "O parâmetro '" + ex.getName() + "' recebeu um valor inválido: '"
                + ex.getValue() + "'.";
        return montar(HttpStatus.BAD_REQUEST, mensagem, request, List.of());
    }

    // 404 - ID inexistente (GET, PUT e DELETE)
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNaoEncontrado(RecursoNaoEncontradoException ex,
                                                            HttpServletRequest request) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of());
    }

    // 404 - Rota que não existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> handleRotaInexistente(NoResourceFoundException ex,
                                                              HttpServletRequest request) {
        return montar(HttpStatus.NOT_FOUND, "Rota não encontrada.", request, List.of());
    }

    // 405 - Método HTTP não suportado na rota
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleMetodo(HttpRequestMethodNotSupportedException ex,
                                                     HttpServletRequest request) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não é suportado nesta rota.", request, List.of());
    }

    // 415 - Content-Type diferente de application/json
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleMediaType(HttpMediaTypeNotSupportedException ex,
                                                        HttpServletRequest request) {
        return montar(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content-Type não suportado. Use application/json.", request, List.of());
    }

    // 409 - Violação de integridade (e-mail/nome duplicado, exclusão de registro com vínculos)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleIntegridade(DataIntegrityViolationException ex,
                                                          HttpServletRequest request) {
        log.warn("Violação de integridade em {} {}: {}", request.getMethod(),
                request.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return montar(HttpStatus.CONFLICT,
                "A operação viola uma restrição de integridade: registro duplicado "
                + "ou vinculado a outros registros.", request, List.of());
    }

    // 422 - Regra de negócio violada
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraNegocio(RegraNegocioException ex,
                                                           HttpServletRequest request) {
        return montar(HttpStatus.valueOf(422), ex.getMessage(), request, List.of());
    }

    // 500 - Qualquer outro erro: mensagem neutra, detalhe só no log do servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(Exception ex, HttpServletRequest request) {
        // Exceções do próprio Spring MVC que já carregam um status HTTP (4xx) são respeitadas
        if (ex instanceof org.springframework.web.ErrorResponse springErro) {
            HttpStatusCode codigo = springErro.getStatusCode();
            HttpStatus status = HttpStatus.resolve(codigo.value());
            if (status != null && status.is4xxClientError()) {
                return montar(status, "Requisição inválida.", request, List.of());
            }
        }
        log.error("Erro inesperado em {} {}", request.getMethod(), request.getRequestURI(), ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Tente novamente mais tarde.", request, List.of());
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem,
                                                HttpServletRequest request,
                                                List<CampoInvalido> campos) {
        ErroResponse corpo = new ErroResponse(
                DataHora.agora(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
