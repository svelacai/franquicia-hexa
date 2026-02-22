package prototipe.franquicia.infrastructure.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import prototipe.franquicia.domain.model.Franquicia;

public interface SpringDataFranquiciaRepository extends R2dbcRepository<Franquicia, Integer> {
}