package prototipe.franquicia.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entidad Franquicia")
public class Franquicia {
    @Id
    @Schema(description = "ID único de la franquicia", example = "1")
    private Integer id; 
    
    @Schema(description = "Nombre de la franquicia", example = "Franquicia A")
    private String nombre;
    
    @Transient // R2DBC ignorara este campo en la persistencia.
    @Schema(description = "Lista de sucursales de la franquicia")
    private List<Sucursal> sucursales;
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
	public List<Sucursal> getSucursales() {
		return sucursales;
	}
	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}
}