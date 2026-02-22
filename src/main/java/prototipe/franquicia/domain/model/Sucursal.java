package prototipe.franquicia.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import io.swagger.v3.oas.annotations.media.Schema;

@Table("sucursal")
@Schema(description = "Entidad Sucursal")
public class Sucursal {
    @Id
    @Schema(description = "ID único de la sucursal", example = "1")
    private Integer id; 
    
    @Schema(description = "Nombre de la sucursal", example = "Sucursal Norte")
    private String nombre;
    
    @Schema(description = "ID de la franquicia a la que pertenece", example = "1")
    private Integer franquiciaId; 
    
    @Transient
    @Schema(description = "Lista de productos de la sucursal")
    private List<Producto> productos;
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
	public Integer getFranquiciaId() {
		return franquiciaId;
	}
	public void setFranquiciaId(Integer franquiciaId) {
		this.franquiciaId = franquiciaId;
	}
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
    
    
}