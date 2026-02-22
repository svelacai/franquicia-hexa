package prototipe.franquicia.infrastructure.persistence;


import org.springframework.data.r2dbc.repository.R2dbcRepository;

import prototipe.franquicia.domain.model.Sucursal;
import reactor.core.publisher.Flux;

public interface SpringDataSucursalRepository extends R2dbcRepository<Sucursal, Integer> {
    Flux<Sucursal> findByFranquiciaId(Integer franquiciaId);
}