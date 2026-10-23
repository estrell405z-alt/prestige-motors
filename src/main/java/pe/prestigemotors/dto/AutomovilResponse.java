package pe.prestigemotors.dto;

import java.math.BigDecimal;

import pe.prestigemotors.model.Automovil;

public record AutomovilResponse(
        Long id,
        String slug,
        String nombre,
        String marca,
        String modelo,
        String tipo,
        String categoria,
        Integer anio,
        Integer kilometraje,
        String color,
        String motor,
        String transmision,
        BigDecimal precio,
        String imagen,
        String descripcion,
        String estado) {

    public static AutomovilResponse from(Automovil a) {
        return new AutomovilResponse(
                a.getId(), a.getSlug(), a.getNombre(), a.getMarca(), a.getModelo(),
                a.getTipo(), a.getCategoria(), a.getAnio(), a.getKilometraje(),
                a.getColor(), a.getMotor(), a.getTransmision(), a.getPrecio(),
                a.getImagen(), a.getDescripcion(), a.getEstado().name());
    }
}
