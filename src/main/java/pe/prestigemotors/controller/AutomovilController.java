package pe.prestigemotors.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.prestigemotors.dto.AutomovilResponse;
import pe.prestigemotors.service.AutomovilService;

@RestController
@RequestMapping("/api/autos")
public class AutomovilController {

    private final AutomovilService service;

    public AutomovilController(AutomovilService service) {
        this.service = service;
    }

    /** Ejemplo: GET /api/autos?q=porsche&tipo=Auto&anio=2021&min=100000&max=300000&orden=precio_asc */
    @GetMapping
    public List<AutomovilResponse> listar(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "marca", required = false) String marca,
            @RequestParam(name = "modelo", required = false) String modelo,
            @RequestParam(name = "anio", required = false) Integer anio,
            @RequestParam(name = "min", required = false) BigDecimal min,
            @RequestParam(name = "max", required = false) BigDecimal max,
            @RequestParam(name = "orden", defaultValue = "fecha_desc") String orden) {
        return service.buscar(q, tipo, marca, modelo, anio, min, max, orden);
    }

    /** Ejemplo: GET /api/autos/porsche-911 (lo usa detalle.html?id=porsche-911) */
    @GetMapping("/{slug}")
    public AutomovilResponse detalle(@PathVariable("slug") String slug) {
        return service.obtenerPorSlug(slug);
    }
}
