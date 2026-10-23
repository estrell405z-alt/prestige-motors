package pe.prestigemotors.dto;

import java.time.LocalDateTime;

import pe.prestigemotors.model.Mantenimiento;

public record MantenimientoResponse(String codigo, String estado, LocalDateTime fechaSolicitud) {

    public static MantenimientoResponse from(Mantenimiento m) {
        return new MantenimientoResponse(m.getCodigo(), m.getEstado().name(), m.getFechaSolicitud());
    }
}
