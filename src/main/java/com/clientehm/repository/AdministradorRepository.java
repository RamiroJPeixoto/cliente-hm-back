package com.clientehm.repository;

import com.clientehm.entity.Administrador; // Import atualizado para o pacote entity
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);
}