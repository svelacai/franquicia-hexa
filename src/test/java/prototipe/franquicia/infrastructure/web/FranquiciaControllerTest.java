package prototipe.franquicia.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(FranquiciaController.class)
public class FranquiciaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranquiciaServicePort franquiciaService;

    private Franquicia franquicia;
    private Sucursal sucursal;
    private Producto producto;

    @BeforeEach
    void setUp() {
        franquicia = new Franquicia();
        franquicia.setId(1);
        franquicia.setNombre("Franquicia Test");
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
        when(franquiciaService.agregarFranquicia(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(franquicia)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class)
                .value(f -> f.getNombre().equals("Franquicia Test"));
    }

    @Test
    void testAgregarSucursal() {
        when(franquiciaService.agregarSucursal(anyInt(), any(Sucursal.class))).thenReturn(Mono.just(franquicia));

        webTestClient.post()
                .uri("/api/franquicias/1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sucursal)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testAgregarProducto() {
        when(franquiciaService.agregarProducto(anyInt(), anyString(), any(Producto.class)))
                .thenReturn(Mono.just(franquicia));

        webTestClient.post()
                .uri("/api/franquicias/1/sucursales/Sucursal Test/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(producto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testEliminarProducto() {
        when(franquiciaService.eliminarProducto(anyInt(), anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        webTestClient.delete()
                .uri("/api/franquicias/1/sucursales/Sucursal Test/productos/Producto Test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testModificarStockProducto() {
        when(franquiciaService.modificarStockProducto(anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.just(franquicia));

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("nuevoStock", 20);

        webTestClient.put()
                .uri("/api/franquicias/1/sucursales/Sucursal Test/productos/Producto Test/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testGetProductoConMasStockPorSucursal() {
        Map<String, Producto> resultado = new HashMap<>();
        resultado.put("Sucursal Test", producto);
        
        when(franquiciaService.getProductoConMasStockPorSucursal(anyInt()))
                .thenReturn(Flux.just(resultado));

        webTestClient.get()
                .uri("/api/franquicias/1/productos-mas-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .hasSize(1);
    }

    @Test
    void testActualizarNombreFranquicia() {
        franquicia.setNombre("Nuevo Nombre");
        when(franquiciaService.actualizarNombreFranquicia(anyInt(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nuevoNombre", "Nuevo Nombre");

        webTestClient.put()
                .uri("/api/franquicias/1/nombre")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class)
                .value(f -> f.getNombre().equals("Nuevo Nombre"));
    }

    @Test
    void testActualizarNombreSucursal() {
        when(franquiciaService.actualizarNombreSucursal(anyInt(), anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nuevoNombre", "Nueva Sucursal");

        webTestClient.put()
                .uri("/api/franquicias/1/sucursales/Sucursal Test/nombre")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testActualizarNombreProducto() {
        when(franquiciaService.actualizarNombreProducto(anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nuevoNombre", "Nuevo Producto");

        webTestClient.put()
                .uri("/api/franquicias/1/sucursales/Sucursal Test/productos/Producto Test/nombre")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franquicia.class);
    }

    @Test
    void testErrorHandling() {
        when(franquiciaService.agregarFranquicia(any(Franquicia.class)))
                .thenReturn(Mono.error(new RuntimeException("Error de prueba")));

        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(franquicia)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}