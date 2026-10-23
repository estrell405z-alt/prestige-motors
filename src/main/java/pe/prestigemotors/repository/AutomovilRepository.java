package pe.prestigemotors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pe.prestigemotors.model.Automovil;

public interface AutomovilRepository extends JpaRepository<Automovil, Long>, JpaSpecificationExecutor<Automovil> {

    Optional<Automovil> findBySlug(String slug);
}
