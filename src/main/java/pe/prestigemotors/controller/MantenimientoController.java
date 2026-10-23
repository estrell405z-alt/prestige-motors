package pe.prestigemotors.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.prestigemotors.dto.MantenimientoRequest;
import pe.prestigemotors.dto.MantenimientoResponse;
import pe.prestigemotors.service.MantenimientoService;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService service;

    public MantenimientoController(MantenimientoService service) {
        this.service = service;
    }

    /** Lo usa el formulario de registro.html. Responde 201 con el código de la solicitud. */
    @PostMapping
    public ResponseEntity<MantenimientoResponse> registrar(@Valid @RequestBody MantenimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }
}
