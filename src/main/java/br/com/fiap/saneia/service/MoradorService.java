package br.com.fiap.saneia.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.saneia.config.DataHora;
import br.com.fiap.saneia.dto.request.MoradorRequest;
import br.com.fiap.saneia.dto.response.MoradorResponse;
import br.com.fiap.saneia.exception.RecursoNaoEncontradoException;
import br.com.fiap.saneia.model.Morador;
import br.com.fiap.saneia.repository.MoradorRepository;

@Service
public class MoradorService {

    private final MoradorRepository repository;
    private final DistritoService distritoService;
    private final ModelMapper modelMapper;

    public MoradorService(MoradorRepository repository, DistritoService distritoService,
                          ModelMapper modelMapper) {
        this.repository = repository;
        this.distritoService = distritoService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public MoradorResponse criar(MoradorRequest request) {
        Morador morador = modelMapper.map(request, Morador.class);
        morador.setId(null);
        // Regra: distrito precisa existir (404 se não existir)
        morador.setDistrito(distritoService.buscarEntidade(request.getDistritoId()));
        // Regra: data de cadastro é definida pela API, não pelo cliente
        morador.setDataCadastro(DataHora.agora());
        normalizar(morador);
        // saveAndFlush: e-mail duplicado estoura aqui -> 409 no handler
        return toResponse(repository.saveAndFlush(morador));
    }

    @Transactional(readOnly = true)
    public List<MoradorResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MoradorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public MoradorResponse atualizar(Long id, MoradorRequest request) {
        Morador existente = buscarEntidade(id); // 404 se não existir
        Morador morador = modelMapper.map(request, Morador.class);
        morador.setId(id);
        morador.setDistrito(distritoService.buscarEntidade(request.getDistritoId()));
        // Regra: data de cadastro original é preservada
        morador.setDataCadastro(existente.getDataCadastro());
        normalizar(morador);
        return toResponse(repository.saveAndFlush(morador));
    }

    @Transactional
    public void excluir(Long id) {
        Morador morador = buscarEntidade(id); // 404 se não existir
        repository.delete(morador);
        // flush: se o morador tiver ocorrências, a FK estoura aqui -> 409
        repository.flush();
    }

    /** Usado pela OcorrenciaService para validar o moradorId recebido. */
    @Transactional(readOnly = true)
    public Morador buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Morador", id));
    }

    private void normalizar(Morador morador) {
        morador.setNome(morador.getNome().trim());
        // Regra: e-mail sempre em minúsculas (evita duplicidade por maiúscula/minúscula)
        morador.setEmail(morador.getEmail().trim().toLowerCase());
    }

    private MoradorResponse toResponse(Morador morador) {
        return modelMapper.map(morador, MoradorResponse.class);
    }
}
