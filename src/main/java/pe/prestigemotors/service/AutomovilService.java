package pe.prestigemotors.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import pe.prestigemotors.dto.AutomovilResponse;
import pe.prestigemotors.exception.RecursoNoEncontradoException;
import pe.prestigemotors.model.Automovil;
import pe.prestigemotors.model.EstadoAutomovil;
import pe.prestigemotors.repository.AutomovilRepository;

@Service
public class AutomovilService {

    private final AutomovilRepository repository;

    public AutomovilService(AutomovilRepository repository) {
        this.repository = repository;
    }

    /** Catálogo con los filtros del formulario de búsqueda de AutoSubasta. Todos son opcionales. */
    @Transactional(readOnly = true)
    public List<AutomovilResponse> buscar(String q, String tipo, String marca, String modelo,
                                          Integer anio, BigDecimal min, BigDecimal max, String orden) {

        Specification<Automovil> filtros = (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();
            condiciones.add(cb.equal(root.get("estado"), EstadoAutomovil.DISPONIBLE));

            if (hayTexto(q)) {
                String patron = patron(q);
                condiciones.add(cb.or(
                        cb.like(cb.lower(root.get("nombre")), patron),
                        cb.like(cb.lower(root.get("marca")), patron),
                        cb.like(cb.lower(root.get("modelo")), patron)));
            }
            if (hayTexto(tipo)) {
                condiciones.add(cb.like(cb.lower(root.get("tipo")), patron(tipo)));
            }
            if (hayTexto(marca)) {
                condiciones.add(cb.like(cb.lower(root.get("marca")), patron(marca)));
            }
            if (hayTexto(modelo)) {
                condiciones.add(cb.like(cb.lower(root.get("modelo")), patron(modelo)));
            }
            if (anio != null) {
                condiciones.add(cb.equal(root.get("anio"), anio));
            }
            if (min != null) {
                condiciones.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("precio"), min));
            }
            if (max != null) {
                condiciones.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("precio"), max));
            }
            return cb.and(condiciones.toArray(new Predicate[0]));
        };

        List<Automovil> resultado = new ArrayList<>(repository.findAll(filtros));
        resultado.sort(comparadorPara(orden));
        return resultado.stream().map(AutomovilResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AutomovilResponse obtenerPorSlug(String slug) {
        return repository.findBySlug(slug)
                .map(AutomovilResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo '" + slug + "'"));
    }

    /** Valores del select "Filtrar por": fecha_desc (por defecto), fecha_asc, precio_asc, precio_desc. */
    private Comparator<Automovil> comparadorPara(String orden) {
        Comparator<Automovil> porFecha = Comparator.comparing(Automovil::getFechaIngreso);
        // Los autos sin precio ("Consultar precio") siempre quedan al final
        Comparator<Automovil> porPrecio = Comparator.comparing(Automovil::getPrecio,
                Comparator.nullsLast(Comparator.<BigDecimal>naturalOrder()));
        Comparator<Automovil> porPrecioDesc = Comparator.comparing(Automovil::getPrecio,
                Comparator.nullsLast(Comparator.<BigDecimal>reverseOrder()));

        return switch (orden == null ? "" : orden) {
            case "fecha_asc" -> porFecha;
            case "precio_asc" -> porPrecio;
            case "precio_desc" -> porPrecioDesc;
            default -> porFecha.reversed();
        };
    }

    private boolean hayTexto(String s) {
        return s != null && !s.isBlank();
    }

    private String patron(String s) {
        return "%" + s.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
