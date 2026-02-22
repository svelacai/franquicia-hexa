package prototipe.franquicia.domain.ports;

import java.util.Map;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaServicePort {
	Mono<Franquicia> agregarFranquicia(Franquicia franquicia);

	Mono<Franquicia> agregarSucursal(Integer franquiciaId, Sucursal sucursal);

	Mono<Franquicia> agregarProducto(Integer franquiciaId, String sucursalNombre, Producto producto);

	Mono<Franquicia> modificarStockProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			Integer nuevoStock);

	Mono<Franquicia> eliminarProducto(Integer franquiciaId, String sucursalNombre, String productoNombre);

	Flux<Producto> findProductosConMasStock(Integer franquiciaId);

	Mono<Franquicia> actualizarNombreFranquicia(Integer franquiciaId, String nuevoNombre);

	Mono<Franquicia> actualizarNombreSucursal(Integer franquiciaId, String sucursalNombre, String nuevoNombre);

	Mono<Franquicia> actualizarNombreProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			String nuevoNombre);
	
    Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(Integer franquiciaId);

}