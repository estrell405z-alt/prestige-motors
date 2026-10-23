package pe.prestigemotors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.prestigemotors.model.Mantenimiento;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
}
