package pe.prestigemotors.service;

import java.time.Year;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.prestigemotors.dto.MantenimientoRequest;
import pe.prestigemotors.dto.MantenimientoResponse;
import pe.prestigemotors.model.Cliente;
import pe.prestigemotors.model.Mantenimiento;
import pe.prestigemotors.repository.ClienteRepository;
import pe.prestigemotors.repository.MantenimientoRepository;

@Service
public class MantenimientoService {

    private final ClienteRepository clienteRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public MantenimientoService(ClienteRepository clienteRepository,
                                MantenimientoRepository mantenimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    /** Registra la solicitud de servicio. Si el correo ya existe reutiliza al cliente; si no, lo crea. */
    @Transactional
    public MantenimientoResponse registrar(MantenimientoRequest req) {
        String correo = req.correo().trim().toLowerCase();

        Cliente cliente = clienteRepository.findByCorreoIgnoreCase(correo).orElseGet(() -> {
            Cliente nuevo = new Cliente();
            nuevo.setNombre(req.nombre().trim());
            nuevo.setApellido(req.apellido().trim());
            nuevo.setCorreo(correo);
            return clienteRepository.save(nuevo);
        });

        Mantenimiento m = new Mantenimiento();
        m.setCliente(cliente);
        m.setPropietario(req.propietario().trim());
        m.setVehiculo(req.modelo().trim());
        m.setPlaca(req.placa().trim().toUpperCase());
        m.setServicio(req.servicio());
        m.setObservaciones(req.observaciones() == null ? null : req.observaciones().trim());

        m = mantenimientoRepository.save(m);
        m.setCodigo(String.format("MT-%d-%05d", Year.now().getValue(), m.getId()));
        return MantenimientoResponse.from(m);
    }
}
