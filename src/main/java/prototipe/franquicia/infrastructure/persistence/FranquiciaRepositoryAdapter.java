package prototipe.franquicia.infrastructure.persistence;

import org.springframework.stereotype.Component;
import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.ports.FranquiciaRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaRepositoryAdapter implements FranquiciaRepositoryPort {

	private final SpringDataFranquiciaRepository franquiciaRepo;
	private final SpringDataSucursalRepository sucursalRepo;
	private final SpringDataProductoRepository productoRepo;

	public FranquiciaRepositoryAdapter(SpringDataFranquiciaRepository franquiciaRepo,
			SpringDataSucursalRepository sucursalRepo, SpringDataProductoRepository productoRepo) {
		this.franquiciaRepo = franquiciaRepo;
		this.sucursalRepo = sucursalRepo;
		this.productoRepo = productoRepo;
	}

	@Override
	public Mono<Franquicia> save(Franquicia franquicia) {
		return franquiciaRepo.save(franquicia).flatMap(franquiciaGuardada -> {
			return Flux.fromIterable(franquicia.getSucursales()).flatMap(sucursal -> {
				sucursal.setFranquiciaId(franquiciaGuardada.getId());
				return sucursalRepo.save(sucursal).flatMap(sucursalGuardada -> {
					return Flux.fromIterable(sucursal.getProductos()).flatMap(producto -> {
						producto.setSucursalId(sucursalGuardada.getId());
						return productoRepo.save(producto).thenReturn(producto);
					}).collectList().map(productosGuardados -> {
						sucursalGuardada.setProductos(productosGuardados);
						return sucursalGuardada;
					});
				});
			}).collectList().map(sucursalesGuardadas -> {
				franquiciaGuardada.setSucursales(sucursalesGuardadas);
				return franquiciaGuardada;
			});
		});
	}

	@Override
	public Mono<Franquicia> findById(Integer id) {
		return franquiciaRepo.findById(id).flatMap(franquicia ->
		sucursalRepo.findByFranquiciaId(franquicia.getId()).flatMap(sucursal ->
		productoRepo.findBySucursalId(sucursal.getId()).collectList().map(productos -> {
			sucursal.setProductos(productos);
			return sucursal;
		})).collectList().map(sucursales -> {
			franquicia.setSucursales(sucursales);
			return franquicia;
		}));
	}
}