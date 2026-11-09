package br.com.fiap.saneia.exception;

/**
 * Lançada pela Service quando a requisição é válida no formato,
 * mas viola uma regra de negócio (ex.: agendar atendimento para
 * ocorrência já RESOLVIDA). Convertida em 422 Unprocessable Entity.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
