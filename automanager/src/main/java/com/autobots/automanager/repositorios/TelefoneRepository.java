package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.entidades.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    
}