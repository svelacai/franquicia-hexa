package prototipe.franquicia.domain.ports;

import prototipe.franquicia.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositoryPort {
	Mono<Franquicia> save(Franquicia franquicia);

	Mono<Franquicia> findById(Integer  id);
}
