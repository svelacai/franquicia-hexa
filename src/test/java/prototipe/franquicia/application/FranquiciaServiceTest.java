package prototipe.franquicia.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaRepositoryPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FranquiciaServiceTest {

	@Mock
	private FranquiciaRepositoryPort franquiciaRepository;

	@InjectMocks
	private FranquiciaService franquiciaService;

	private Franquicia franquicia;
	private Sucursal sucursal;
	private Producto producto;

	@BeforeEach
	void setUp() {
		franquicia = new Franquicia();
		franquicia.setId(1);
		franquicia.setNombre("Franquicia de Prueba");
		franquicia.setSucursales(new ArrayList<>());

		sucursal = new Sucursal();
		sucursal.setNombre("Sucursal Test");
		sucursal.setProductos(new ArrayList<>());

		producto = new Producto();
		producto.setNombre("Producto Test");
		producto.setStock(10);
	}

	@Test
	void testAgregarFranquicia() {
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarFranquicia(franquicia))
				.expectNextMatches(f -> f.getNombre().equals("Franquicia de Prueba")).verifyComplete();
	}

	@Test
	void testAgregarSucursal() {
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarSucursal(1, sucursal))
				.expectNextMatches(f -> f.getSucursales().size() == 1 && f.getSucursales().get(0).getNombre().equals("Sucursal Test"))
				.verifyComplete();
	}

	@Test
	void testAgregarProducto() {
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarProducto(1, "Sucursal Test", producto))
				.expectNextMatches(f -> f.getSucursales().get(0).getProductos().size() == 1)
				.verifyComplete();
	}

	@Test
	void testModificarStockProducto() {
		sucursal.setProductos(Arrays.asList(producto));
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.modificarStockProducto(1, "Sucursal Test", "Producto Test", 20))
				.expectNextMatches(f -> f.getSucursales().get(0).getProductos().get(0).getStock() == 20)
				.verifyComplete();
	}

	@Test
	void testEliminarProducto() {
		sucursal.setProductos(new ArrayList<>(Arrays.asList(producto)));
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.eliminarProducto(1, "Sucursal Test", "Producto Test"))
				.expectNextMatches(f -> f.getSucursales().get(0).getProductos().isEmpty())
				.verifyComplete();
	}

	@Test
	void testActualizarNombreFranquicia() {
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.actualizarNombreFranquicia(1, "Nuevo Nombre"))
				.expectNextMatches(f -> f.getNombre().equals("Nuevo Nombre"))
				.verifyComplete();
	}

	@Test
	void testGetProductoConMasStockPorSucursal() {
		Producto producto2 = new Producto();
		producto2.setNombre("Producto 2");
		producto2.setStock(5);
		sucursal.setProductos(Arrays.asList(producto, producto2));
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.getProductoConMasStockPorSucursal(1))
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testActualizarNombreSucursal() {
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.actualizarNombreSucursal(1, "Sucursal Test", "Nueva Sucursal"))
				.expectNextMatches(f -> f.getSucursales().get(0).getNombre().equals("Nueva Sucursal"))
				.verifyComplete();
	}

	@Test
	void testActualizarNombreProducto() {
		sucursal.setProductos(Arrays.asList(producto));
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));
		when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.actualizarNombreProducto(1, "Sucursal Test", "Producto Test", "Nuevo Producto"))
				.expectNextMatches(f -> f.getSucursales().get(0).getProductos().get(0).getNombre().equals("Nuevo Producto"))
				.verifyComplete();
	}

	@Test
	void testSucursalNoEncontrada() {
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.agregarProducto(1, "Sucursal Inexistente", producto))
				.verifyComplete();
	}

	@Test
	void testProductoNoEncontrado() {
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.modificarStockProducto(1, "Sucursal Test", "Producto Inexistente", 20))
				.verifyComplete();
	}

	@Test
	void testGetProductoConMasStockSinProductos() {
		franquicia.setSucursales(Arrays.asList(sucursal));
		when(franquiciaRepository.findById(1)).thenReturn(Mono.just(franquicia));

		StepVerifier.create(franquiciaService.getProductoConMasStockPorSucursal(1))
				.verifyComplete();
	}
}