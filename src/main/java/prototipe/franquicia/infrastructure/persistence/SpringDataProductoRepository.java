package prototipe.franquicia.infrastructure.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import prototipe.franquicia.domain.model.Producto;
import reactor.core.publisher.Flux;

public interface SpringDataProductoRepository extends R2dbcRepository<Producto, Integer> {
    Flux<Producto> findBySucursalId(Integer sucursalId);
}