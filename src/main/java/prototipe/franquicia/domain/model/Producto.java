package prototipe.franquicia.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import io.swagger.v3.oas.annotations.media.Schema;

@Table("producto")
@Schema(description = "Entidad Producto")
public class Producto {
    @Id
    @Schema(description = "ID único del producto", example = "1")
    private Integer id; 
    
    @Schema(description = "Nombre del producto", example = "Hamburguesa Sencilla")
    private String nombre;
    
    @Schema(description = "Stock disponible del producto", example = "50")
    private Integer stock;
    
    @Schema(description = "ID de la sucursal a la que pertenece", example = "1")
    private Integer sucursalId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getSucursalId() {
		return sucursalId;
	}
	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	} 
    
}