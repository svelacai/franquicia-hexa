package prototipe.franquicia.infrastructure.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private static final Logger log = LoggerFactory.getLogger(FranquiciaController.class);

    private final FranquiciaServicePort franquiciaService;

    @Autowired
    public FranquiciaController(FranquiciaServicePort franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping
    public Mono<Franquicia> agregarFranquicia(@RequestBody Franquicia franquicia) {
        log.info("POST /api/franquicias - Creando nueva franquicia: {}", franquicia.getNombre());
        return franquiciaService.agregarFranquicia(franquicia)
                .doOnSuccess(f -> log.info("Franquicia creada exitosamente con ID: {}", f.getId()))
                .doOnError(error -> log.error("Error en POST /api/franquicias: {}", error.getMessage()));
    }

    @PostMapping("/{franquiciaId}/sucursales")
    public Mono<Franquicia> agregarSucursal(@PathVariable Integer franquiciaId, @RequestBody Sucursal sucursal) {
        log.info("POST /api/franquicias/{}/sucursales - Agregando sucursal: {}", franquiciaId, sucursal.getNombre());
        return franquiciaService.agregarSucursal(franquiciaId, sucursal)
                .doOnSuccess(f -> log.info("Sucursal agregada exitosamente a franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("Error al agregar sucursal: {}", error.getMessage()));
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos")
    public Mono<Franquicia> agregarProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @RequestBody Producto producto) {
        return franquiciaService.agregarProducto(franquiciaId, sucursalNombre, producto);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}")
    public Mono<Franquicia> eliminarProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre) {
        log.info("DELETE /api/franquicias/{}/sucursales/{}/productos/{} - Eliminando producto", 
                franquiciaId, sucursalNombre, productoNombre);
        return franquiciaService.eliminarProducto(franquiciaId, sucursalNombre, productoNombre)
                .doOnSuccess(f -> log.info("Producto eliminado exitosamente"))
                .doOnError(error -> log.error("Error al eliminar producto: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock")
    public Mono<Franquicia> modificarStockProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre, @RequestBody Map<String, Integer> requestBody) {
        int nuevoStock = requestBody.get("nuevoStock");
        log.info("PUT /api/franquicias/{}/sucursales/{}/productos/{}/stock - Nuevo stock: {}", 
                franquiciaId, sucursalNombre, productoNombre, nuevoStock);
        return franquiciaService.modificarStockProducto(franquiciaId, sucursalNombre, productoNombre, nuevoStock)
                .doOnSuccess(f -> log.info("Stock actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar stock: {}", error.getMessage()));
    }

    @GetMapping("/{franquiciaId}/productos-mas-stock")
    public Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(@PathVariable Integer franquiciaId) {
        log.info("GET /api/franquicias/{}/productos-mas-stock - Consultando productos con más stock", franquiciaId);
        return franquiciaService.getProductoConMasStockPorSucursal(franquiciaId)
                .doOnComplete(() -> log.info("Consulta de productos completada para franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("Error en consulta de productos: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/nombre")
    public Mono<Franquicia> actualizarNombreFranquicia(@PathVariable Integer franquiciaId, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/nombre")
    public Mono<Franquicia> actualizarNombreSucursal(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalNombre, nuevoNombre);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/nombre")
    public Mono<Franquicia> actualizarNombreProducto(@PathVariable Integer franquiciaId, @PathVariable String sucursalNombre, @PathVariable String productoNombre, @RequestBody Map<String, String> requestBody) {
        String nuevoNombre = requestBody.get("nuevoNombre");
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalNombre, productoNombre, nuevoNombre);
    }
}