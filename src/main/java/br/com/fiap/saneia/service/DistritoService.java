package br.com.fiap.saneia.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.saneia.dto.request.DistritoRequest;
import br.com.fiap.saneia.dto.response.DistritoResponse;
import br.com.fiap.saneia.exception.RecursoNaoEncontradoException;
import br.com.fiap.saneia.model.Distrito;
import br.com.fiap.saneia.repository.DistritoRepository;

@Service
public class DistritoService {

    private final DistritoRepository repository;
    private final ModelMapper modelMapper;

    public DistritoService(DistritoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public DistritoResponse criar(DistritoRequest request) {
        Distrito distrito = modelMapper.map(request, Distrito.class);
        distrito.setId(null);
        normalizar(distrito);
        // saveAndFlush: nome duplicado estoura aqui -> 409 no handler
        return toResponse(repository.saveAndFlush(distrito));
    }

    @Transactional(readOnly = true)
    public List<DistritoResponse> listar() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DistritoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public DistritoResponse atualizar(Long id, DistritoRequest request) {
        buscarEntidade(id); // 404 se não existir
        Distrito distrito = modelMapper.map(request, Distrito.class);
        distrito.setId(id);
        normalizar(distrito);
        return toResponse(repository.saveAndFlush(distrito));
    }

    @Transactional
    public void excluir(Long id) {
        Distrito distrito = buscarEntidade(id); // 404 se não existir
        repository.delete(distrito);
        // flush: se houver moradores/ocorrências vinculados, a FK estoura aqui -> 409
        repository.flush();
    }

    /** Usado pelas outras Services para validar o distritoId recebido. */
    @Transactional(readOnly = true)
    public Distrito buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Distrito", id));
    }

    private void normalizar(Distrito distrito) {
        distrito.setNome(distrito.getNome().trim());
        distrito.setSubprefeitura(distrito.getSubprefeitura().trim());
    }

    private DistritoResponse toResponse(Distrito distrito) {
        return modelMapper.map(distrito, DistritoResponse.class);
    }
}
