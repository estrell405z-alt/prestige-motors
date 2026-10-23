package pe.prestigemotors.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.prestigemotors.model.TipoServicio;

/** Datos que llegan desde registro.html (los nombres coinciden con los name="" del formulario). */
public record MantenimientoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "El nombre es demasiado largo")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 60, message = "El apellido es demasiado largo")
        String apellido,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 120, message = "El correo es demasiado largo")
        String correo,

        @NotBlank(message = "El nombre del propietario es obligatorio")
        @Size(max = 120, message = "El nombre del propietario es demasiado largo")
        String propietario,

        @NotBlank(message = "La marca y modelo son obligatorios")
        @Size(max = 120, message = "La marca y modelo son demasiado largos")
        String modelo,

        @NotBlank(message = "La placa es obligatoria")
        @Pattern(regexp = "^[A-Za-z0-9]{3}-[A-Za-z0-9]{3}$", message = "La placa debe tener el formato ABC-123")
        String placa,

        @NotNull(message = "Selecciona el servicio a realizar")
        TipoServicio servicio,

        @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
        String observaciones) {
}
