package br.com.fiap.saneia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.saneia.model.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {
}
