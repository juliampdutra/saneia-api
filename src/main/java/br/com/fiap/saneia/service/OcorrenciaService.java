package br.com.fiap.saneia.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.saneia.config.DataHora;
import br.com.fiap.saneia.dto.request.OcorrenciaRequest;
import br.com.fiap.saneia.dto.response.OcorrenciaResponse;
import br.com.fiap.saneia.exception.RecursoNaoEncontradoException;
import br.com.fiap.saneia.model.Ocorrencia;
import br.com.fiap.saneia.model.enums.StatusOcorrencia;
import br.com.fiap.saneia.repository.OcorrenciaRepository;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository repository;
    private final MoradorService moradorService;
    private final DistritoService distritoService;
    private final ModelMapper modelMapper;

    public OcorrenciaService(OcorrenciaRepository repository, MoradorService moradorService,
                             DistritoService distritoService, ModelMapper modelMapper) {
        this.repository = repository;
        this.moradorService = moradorService;
        this.distritoService = distritoService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public OcorrenciaResponse criar(OcorrenciaRequest request) {
        Ocorrencia ocorrencia = modelMapper.map(request, Ocorrencia.class);
        ocorrencia.setId(null);
        // Regra: morador e distrito precisam existir (404 se não existirem)
        ocorrencia.setMorador(moradorService.buscarEntidade(request.getMoradorId()));
        ocorrencia.setDistrito(distritoService.buscarEntidade(request.getDistritoId()));
        // Regra: toda ocorrência nasce ABERTA, com data de abertura definida pela API
        ocorrencia.setStatus(StatusOcorrencia.ABERTA);
        ocorrencia.setDataAbertura(DataHora.agora());
        ocorrencia.setDataResolucao(null);
        return toResponse(repository.saveAndFlush(ocorrencia));
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OcorrenciaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public OcorrenciaResponse atualizar(Long id, OcorrenciaRequest request) {
        Ocorrencia existente = buscarEntidade(id); // 404 se não existir
        Ocorrencia ocorrencia = modelMapper.map(request, Ocorrencia.class);
        ocorrencia.setId(id);
        ocorrencia.setMorador(moradorService.buscarEntidade(request.getMoradorId()));
        ocorrencia.setDistrito(distritoService.buscarEntidade(request.getDistritoId()));
        // Regra: data de abertura original é preservada
        ocorrencia.setDataAbertura(existente.getDataAbertura());

        // Regra: se o status não for enviado, mantém o atual
        StatusOcorrencia novoStatus = request.getStatus() != null
                ? request.getStatus()
                : existente.getStatus();
        ocorrencia.setStatus(novoStatus);

        // Regra: data de resolução só existe quando a ocorrência está RESOLVIDA
        if (novoStatus == StatusOcorrencia.RESOLVIDA) {
            ocorrencia.setDataResolucao(existente.getDataResolucao() != null
                    ? existente.getDataResolucao()
                    : DataHora.agora());
        } else {
            ocorrencia.setDataResolucao(null);
        }
        return toResponse(repository.saveAndFlush(ocorrencia));
    }

    @Transactional
    public void excluir(Long id) {
        Ocorrencia ocorrencia = buscarEntidade(id); // 404 se não existir
        repository.delete(ocorrencia);
        // flush: se a ocorrência tiver atendimentos, a FK estoura aqui -> 409
        repository.flush();
    }

    /** Usado pela AtendimentoService para validar o ocorrenciaId recebido. */
    @Transactional(readOnly = true)
    public Ocorrencia buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ocorrência", id));
    }

    private OcorrenciaResponse toResponse(Ocorrencia ocorrencia) {
        return modelMapper.map(ocorrencia, OcorrenciaResponse.class);
    }
}
