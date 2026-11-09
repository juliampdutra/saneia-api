package br.com.fiap.saneia.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.saneia.config.DataHora;
import br.com.fiap.saneia.dto.request.AtendimentoRequest;
import br.com.fiap.saneia.dto.response.AtendimentoResponse;
import br.com.fiap.saneia.exception.RecursoNaoEncontradoException;
import br.com.fiap.saneia.exception.RegraNegocioException;
import br.com.fiap.saneia.model.Atendimento;
import br.com.fiap.saneia.model.Ocorrencia;
import br.com.fiap.saneia.model.enums.StatusAtendimento;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import br.com.fiap.saneia.repository.AtendimentoRepository;

@Service
public class AtendimentoService {

    private final AtendimentoRepository repository;
    private final OcorrenciaService ocorrenciaService;
    private final ModelMapper modelMapper;

    public AtendimentoService(AtendimentoRepository repository, OcorrenciaService ocorrenciaService,
                              ModelMapper modelMapper) {
        this.repository = repository;
        this.ocorrenciaService = ocorrenciaService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AtendimentoResponse criar(AtendimentoRequest request) {
        Ocorrencia ocorrencia = ocorrenciaService.buscarEntidade(request.getOcorrenciaId()); // 404
        // Regra: não se agenda atendimento para ocorrência já encerrada -> 422
        validarOcorrenciaEmAberto(ocorrencia);

        Atendimento atendimento = modelMapper.map(request, Atendimento.class);
        atendimento.setId(null);
        atendimento.setOcorrencia(ocorrencia);
        // Regra: se o status não for informado, o atendimento começa AGENDADO
        atendimento.setStatus(request.getStatus() != null
                ? request.getStatus()
                : StatusAtendimento.AGENDADO);
        atendimento.setDataConclusao(null);

        aplicarEfeitosDoStatus(atendimento, ocorrencia);
        return toResponse(repository.saveAndFlush(atendimento));
    }

    @Transactional(readOnly = true)
    public List<AtendimentoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AtendimentoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public AtendimentoResponse atualizar(Long id, AtendimentoRequest request) {
        Atendimento existente = buscarEntidade(id); // 404 se não existir
        Ocorrencia ocorrencia = ocorrenciaService.buscarEntidade(request.getOcorrenciaId()); // 404

        // Regra: só pode mover o atendimento para outra ocorrência se ela estiver em aberto
        boolean trocouOcorrencia = !existente.getOcorrencia().getId().equals(ocorrencia.getId());
        if (trocouOcorrencia) {
            validarOcorrenciaEmAberto(ocorrencia);
        }

        Atendimento atendimento = modelMapper.map(request, Atendimento.class);
        atendimento.setId(id);
        atendimento.setOcorrencia(ocorrencia);
        // Regra: se o status não for enviado, mantém o atual
        atendimento.setStatus(request.getStatus() != null
                ? request.getStatus()
                : existente.getStatus());
        atendimento.setDataConclusao(existente.getDataConclusao());

        aplicarEfeitosDoStatus(atendimento, ocorrencia);
        return toResponse(repository.saveAndFlush(atendimento));
    }

    @Transactional
    public void excluir(Long id) {
        Atendimento atendimento = buscarEntidade(id); // 404 se não existir
        repository.delete(atendimento);
        repository.flush();
    }

    @Transactional(readOnly = true)
    public Atendimento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento", id));
    }

    private void validarOcorrenciaEmAberto(Ocorrencia ocorrencia) {
        if (ocorrencia.getStatus() == StatusOcorrencia.RESOLVIDA
                || ocorrencia.getStatus() == StatusOcorrencia.CANCELADA) {
            throw new RegraNegocioException("Não é possível agendar atendimento para a ocorrência "
                    + ocorrencia.getId() + ", pois ela está " + ocorrencia.getStatus() + ".");
        }
    }

    /**
     * Regras que ligam o status do atendimento ao status da ocorrência:
     * - CONCLUIDO: registra a data de conclusão e marca a ocorrência como RESOLVIDA.
     * - AGENDADO / EM_ANDAMENTO: a ocorrência passa a EM_ATENDIMENTO.
     * - CANCELADO: não altera a ocorrência.
     */
    private void aplicarEfeitosDoStatus(Atendimento atendimento, Ocorrencia ocorrencia) {
        switch (atendimento.getStatus()) {
            case CONCLUIDO -> {
                if (atendimento.getDataConclusao() == null) {
                    atendimento.setDataConclusao(DataHora.agora());
                }
                ocorrencia.setStatus(StatusOcorrencia.RESOLVIDA);
                if (ocorrencia.getDataResolucao() == null) {
                    ocorrencia.setDataResolucao(DataHora.agora());
                }
            }
            case AGENDADO, EM_ANDAMENTO -> {
                atendimento.setDataConclusao(null);
                if (ocorrencia.getStatus() != StatusOcorrencia.CANCELADA) {
                    ocorrencia.setStatus(StatusOcorrencia.EM_ATENDIMENTO);
                    ocorrencia.setDataResolucao(null);
                }
            }
            case CANCELADO -> atendimento.setDataConclusao(null);
        }
    }

    private AtendimentoResponse toResponse(Atendimento atendimento) {
        return modelMapper.map(atendimento, AtendimentoResponse.class);
    }
}
