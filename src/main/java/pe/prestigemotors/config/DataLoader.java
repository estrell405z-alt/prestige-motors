package pe.prestigemotors.config;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.prestigemotors.model.Automovil;
import pe.prestigemotors.repository.AutomovilRepository;

/**
 * Carga el catálogo inicial desde src/main/resources/seed/autos.json
 * la primera vez que arranca (solo si la tabla automovil está vacía).
 * Para agregar vehículos de prueba, edita ese JSON y borra la tabla, o usa el panel admin cuando lo tengas.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final AutomovilRepository repository;
    private final ObjectMapper mapper;

    public DataLoader(AutomovilRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public record AutoSeed(String slug, String nombre, String marca, String modelo, String tipo, String categoria,
                    Integer anio, Integer kilometraje, String color, String motor, String transmision,
                    BigDecimal precio, String imagen, String descripcion) {
    }

    @Override
    public void run(String... args) throws IOException {
        if (repository.count() > 0) {
            return;
        }
        List<AutoSeed> semillas;
        try (InputStream in = new ClassPathResource("seed/autos.json").getInputStream()) {
            semillas = mapper.readValue(in, new TypeReference<List<AutoSeed>>() { });
        }

        LocalDate hoy = LocalDate.now();
        List<Automovil> autos = new ArrayList<>();
        for (int i = 0; i < semillas.size(); i++) {
            AutoSeed s = semillas.get(i);
            Automovil a = new Automovil();
            a.setSlug(s.slug());
            a.setNombre(s.nombre());
            a.setMarca(s.marca());
            a.setModelo(s.modelo());
            a.setTipo(s.tipo());
            a.setCategoria(s.categoria());
            a.setAnio(s.anio());
            a.setKilometraje(s.kilometraje());
            a.setColor(s.color());
            a.setMotor(s.motor());
            a.setTransmision(s.transmision());
            a.setPrecio(s.precio());
            a.setImagen(s.imagen());
            a.setDescripcion(s.descripcion());
            a.setFechaIngreso(hoy.minusDays(i)); // el primero del JSON es el más reciente
            autos.add(a);
        }
        repository.saveAll(autos);
        System.out.println(">> Catálogo inicial cargado: " + autos.size() + " vehículos");
    }
}
