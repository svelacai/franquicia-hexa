package prototipe.franquicia.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaRepositoryPort;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class FranquiciaService implements FranquiciaServicePort {

	private static final Logger log = LoggerFactory.getLogger(FranquiciaService.class);

	private final FranquiciaRepositoryPort franquiciaRepository;

	public FranquiciaService(FranquiciaRepositoryPort franquiciaRepository) {
		this.franquiciaRepository = franquiciaRepository;
	}

	@Override
	public Mono<Franquicia> agregarFranquicia(Franquicia franquicia) {
		log.info("Agregando nueva franquicia: {}", franquicia.getNombre());
		return franquiciaRepository.save(franquicia)
				.doOnSuccess(f -> log.info("Franquicia agregada exitosamente con ID: {}", f.getId()))
				.doOnError(error -> log.error("Error al agregar franquicia: {}", error.getMessage()));
	}

	@Override
	public Mono<Franquicia> agregarSucursal(Integer franquiciaId, Sucursal sucursal) {
		log.info("Agregando sucursal '{}' a franquicia ID: {}", sucursal.getNombre(), franquiciaId);
		return franquiciaRepository.findById(franquiciaId)
				.doOnNext(f -> log.debug("Franquicia encontrada: {}", f.getNombre()))
				.flatMap(franquicia -> {
					franquicia.getSucursales().add(sucursal);
					return franquiciaRepository.save(franquicia);
				})
				.doOnSuccess(f -> log.info("Sucursal agregada exitosamente a franquicia ID: {}", franquiciaId))
				.doOnError(error -> log.error("Error al agregar sucursal: {}", error.getMessage()));
	}

	@Override
	public Mono<Franquicia> agregarProducto(Integer franquiciaId, String sucursalNombre, Producto producto) {
		log.info("Agregando producto '{}' a sucursal '{}' de franquicia ID: {}", 
				producto.getNombre(), sucursalNombre, franquiciaId);
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				sucursalEncontrada.getProductos().add(producto);
				log.debug("Producto agregado a sucursal: {}", sucursalNombre);
				return franquiciaRepository.save(franquicia);
			} else {
				log.warn("Sucursal '{}' no encontrada en franquicia ID: {}", sucursalNombre, franquiciaId);
			}
			return Mono.empty();
		})
		.doOnSuccess(f -> log.info("Producto agregado exitosamente"))
		.doOnError(error -> log.error("Error al agregar producto: {}", error.getMessage()));
	}

	@Override
	public Mono<Franquicia> modificarStockProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			Integer nuevoStock) {
		log.info("Modificando stock del producto '{}' a {} en sucursal '{}'", 
				productoNombre, nuevoStock, sucursalNombre);
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				Producto productoEncontrado = sucursalEncontrada.getProductos().stream()
						.filter(p -> p.getNombre().equals(productoNombre)).findFirst().orElse(null);
				if (productoEncontrado != null) {
					Integer stockAnterior = productoEncontrado.getStock();
					productoEncontrado.setStock(nuevoStock);
					log.debug("Stock modificado de {} a {} para producto '{}'", 
							stockAnterior, nuevoStock, productoNombre);
					return franquiciaRepository.save(franquicia);
				} else {
					log.warn("Producto '{}' no encontrado en sucursal '{}'", productoNombre, sucursalNombre);
				}
			} else {
				log.warn("Sucursal '{}' no encontrada", sucursalNombre);
			}
			return Mono.empty();
		})
		.doOnSuccess(f -> log.info("Stock modificado exitosamente"))
		.doOnError(error -> log.error("Error al modificar stock: {}", error.getMessage()));
	}

	@Override
	public Mono<Franquicia> eliminarProducto(Integer franquiciaId, String sucursalNombre, String productoNombre) {
		log.info("Eliminando producto '{}' de sucursal '{}'", productoNombre, sucursalNombre);
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				boolean eliminado = sucursalEncontrada.getProductos().removeIf(p -> p.getNombre().equals(productoNombre));
				if (eliminado) {
					log.debug("Producto '{}' eliminado de sucursal '{}'", productoNombre, sucursalNombre);
				} else {
					log.warn("Producto '{}' no encontrado para eliminar", productoNombre);
				}
				return franquiciaRepository.save(franquicia);
			} else {
				log.warn("Sucursal '{}' no encontrada", sucursalNombre);
			}
			return Mono.empty();
		})
		.doOnSuccess(f -> log.info("Producto eliminado exitosamente"))
		.doOnError(error -> log.error("Error al eliminar producto: {}", error.getMessage()));
	}

	@Override
	public Flux<Producto> findProductosConMasStock(Integer franquiciaId) {
		return franquiciaRepository.findById(franquiciaId)
				.flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
				.flatMap(sucursal -> Flux.fromIterable(sucursal.getProductos())
						.sort(Comparator.comparing(Producto::getStock).reversed()).take(1));
	}

	@Override
	public Mono<Franquicia> actualizarNombreFranquicia(Integer franquiciaId, String nuevoNombre) {
		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			franquicia.setNombre(nuevoNombre);
			return franquiciaRepository.save(franquicia);
		});
	}

	@Override
	public Mono<Franquicia> actualizarNombreSucursal(Integer franquiciaId, String sucursalNombre, String nuevoNombre) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				sucursalEncontrada.setNombre(nuevoNombre);
				return franquiciaRepository.save(franquicia);
			}
			return Mono.empty();
		});
	}

	@Override
	public Mono<Franquicia> actualizarNombreProducto(Integer franquiciaId, String sucursalNombre, String productoNombre,
			String nuevoNombre) {

		return franquiciaRepository.findById(franquiciaId).flatMap(franquicia -> {
			Sucursal sucursalEncontrada = franquicia.getSucursales().stream()
					.filter(s -> s.getNombre().equals(sucursalNombre)).findFirst().orElse(null);
			if (sucursalEncontrada != null) {
				Producto productoEncontrado = sucursalEncontrada.getProductos().stream()
						.filter(p -> p.getNombre().equals(productoNombre)).findFirst().orElse(null);
				if (productoEncontrado != null) {
					productoEncontrado.setNombre(nuevoNombre);
					return franquiciaRepository.save(franquicia);
				}
			}
			return Mono.empty();
		});
	}

	@Override
	public Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(Integer franquiciaId) {
		log.info("Consultando productos con más stock por sucursal para franquicia ID: {}", franquiciaId);
		return franquiciaRepository.findById(franquiciaId)
				.doOnNext(f -> log.debug("Procesando {} sucursales", f.getSucursales().size()))
				.flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()).flatMap(sucursal -> {
					Producto productoMasStock = sucursal.getProductos().stream()
							.max(Comparator.comparing(Producto::getStock)).orElse(null);
					if (productoMasStock != null) {
						log.debug("Producto con más stock en '{}': {} (stock: {})", 
								sucursal.getNombre(), productoMasStock.getNombre(), productoMasStock.getStock());
						Map<String, Producto> resultado = new HashMap<>();
						resultado.put(sucursal.getNombre(), productoMasStock);
						return Mono.just(resultado);
					}
					return Mono.empty();
				}))
				.doOnComplete(() -> log.info("Consulta de productos con más stock completada"))
				.doOnError(error -> log.error("Error al consultar productos con más stock: {}", error.getMessage()));
	}
}