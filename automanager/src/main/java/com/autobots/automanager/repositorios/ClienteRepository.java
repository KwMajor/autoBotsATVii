package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.entidades.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}