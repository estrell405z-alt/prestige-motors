package pe.prestigemotors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.prestigemotors.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCorreoIgnoreCase(String correo);
}
