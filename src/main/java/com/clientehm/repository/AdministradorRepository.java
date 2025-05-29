package com.clientehm.repository;

import com.clientehm.entity.Administrador; // Import da entidade no pacote correto
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);
}