package prototipe.franquicia.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para actualizar nombre")
public class NameUpdateRequest {
    
    @Schema(description = "Nuevo nombre", example = "Franquicia B")
    private String nuevoNombre;

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }
}