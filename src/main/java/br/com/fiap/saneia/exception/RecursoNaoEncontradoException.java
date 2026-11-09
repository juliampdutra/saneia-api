package br.com.fiap.saneia.exception;

/**
 * Lançada pela Service quando um ID não existe no banco.
 * O GlobalExceptionHandler converte em 404 Not Found.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado(a)");
    }
}
