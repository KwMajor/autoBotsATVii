package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.entidades.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

}