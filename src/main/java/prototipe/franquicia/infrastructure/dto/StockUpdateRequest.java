package prototipe.franquicia.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para actualizar stock de producto")
public class StockUpdateRequest {
    
    @Schema(description = "Nuevo stock del producto", example = "75")
    private Integer nuevoStock;

    public Integer getNuevoStock() {
        return nuevoStock;
    }

    public void setNuevoStock(Integer nuevoStock) {
        this.nuevoStock = nuevoStock;
    }
}