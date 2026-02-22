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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import prototipe.franquicia.domain.model.Franquicia;
import prototipe.franquicia.domain.model.Producto;
import prototipe.franquicia.domain.model.Sucursal;
import prototipe.franquicia.domain.ports.FranquiciaServicePort;
import prototipe.franquicia.infrastructure.dto.NameUpdateRequest;
import prototipe.franquicia.infrastructure.dto.StockUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@Tag(name = "Franquicias", description = "API para gestión de franquicias, sucursales y productos")
public class FranquiciaController {

    private static final Logger log = LoggerFactory.getLogger(FranquiciaController.class);

    private final FranquiciaServicePort franquiciaService;

    @Autowired
    public FranquiciaController(FranquiciaServicePort franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping
    @Operation(summary = "Crear franquicia", description = "Agrega una nueva franquicia al sistema")
    public Mono<Franquicia> agregarFranquicia(@RequestBody Franquicia franquicia) {
        log.info("POST /api/franquicias - Creando nueva franquicia: {}", franquicia.getNombre());
        return franquiciaService.agregarFranquicia(franquicia)
                .doOnSuccess(f -> log.info("Franquicia creada exitosamente con ID: {}", f.getId()))
                .doOnError(error -> log.error("Error en POST /api/franquicias: {}", error.getMessage()));
    }

    @PostMapping("/{franquiciaId}/sucursales")
    @Operation(summary = "Agregar sucursal", description = "Agrega una nueva sucursal a una franquicia existente")
    public Mono<Franquicia> agregarSucursal(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @RequestBody Sucursal sucursal) {
        log.info("POST /api/franquicias/{}/sucursales - Agregando sucursal: {}", franquiciaId, sucursal.getNombre());
        return franquiciaService.agregarSucursal(franquiciaId, sucursal)
                .doOnSuccess(f -> log.info("Sucursal agregada exitosamente a franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("Error al agregar sucursal: {}", error.getMessage()));
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos")
    @Operation(summary = "Agregar producto", description = "Agrega un nuevo producto a una sucursal específica")
    public Mono<Franquicia> agregarProducto(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @Parameter(description = "Nombre de la sucursal") @PathVariable String sucursalNombre, 
            @RequestBody Producto producto) {
        log.info("POST /api/franquicias/{}/sucursales/{}/productos - Agregando producto: {}", 
                franquiciaId, sucursalNombre, producto.getNombre());
        return franquiciaService.agregarProducto(franquiciaId, sucursalNombre, producto)
                .doOnSuccess(f -> log.info("Producto agregado exitosamente"))
                .doOnError(error -> log.error("Error al agregar producto: {}", error.getMessage()));
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto de una sucursal específica")
    public Mono<Franquicia> eliminarProducto(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @Parameter(description = "Nombre de la sucursal") @PathVariable String sucursalNombre, 
            @Parameter(description = "Nombre del producto") @PathVariable String productoNombre) {
        log.info("DELETE /api/franquicias/{}/sucursales/{}/productos/{} - Eliminando producto", 
                franquiciaId, sucursalNombre, productoNombre);
        return franquiciaService.eliminarProducto(franquiciaId, sucursalNombre, productoNombre)
                .doOnSuccess(f -> log.info("Producto eliminado exitosamente"))
                .doOnError(error -> log.error("Error al eliminar producto: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/stock")
    @Operation(summary = "Modificar stock", description = "Modifica el stock de un producto específico")
    public Mono<Franquicia> modificarStockProducto(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @Parameter(description = "Nombre de la sucursal") @PathVariable String sucursalNombre, 
            @Parameter(description = "Nombre del producto") @PathVariable String productoNombre, 
            @RequestBody StockUpdateRequest request) {
        int nuevoStock = request.getNuevoStock();
        log.info("PUT /api/franquicias/{}/sucursales/{}/productos/{}/stock - Nuevo stock: {}", 
                franquiciaId, sucursalNombre, productoNombre, nuevoStock);
        return franquiciaService.modificarStockProducto(franquiciaId, sucursalNombre, productoNombre, nuevoStock)
                .doOnSuccess(f -> log.info("Stock actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar stock: {}", error.getMessage()));
    }

    @GetMapping("/{franquiciaId}/productos-mas-stock")
    @Operation(summary = "Productos con más stock", description = "Obtiene el producto con más stock por cada sucursal de una franquicia")
    public Flux<Map<String, Producto>> getProductoConMasStockPorSucursal(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId) {
        log.info("GET /api/franquicias/{}/productos-mas-stock - Consultando productos con más stock", franquiciaId);
        return franquiciaService.getProductoConMasStockPorSucursal(franquiciaId)
                .doOnComplete(() -> log.info("Consulta de productos completada para franquicia ID: {}", franquiciaId))
                .doOnError(error -> log.error("Error en consulta de productos: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/nombre")
    @Operation(summary = "Actualizar nombre de franquicia", description = "Actualiza el nombre de una franquicia específica")
    public Mono<Franquicia> actualizarNombreFranquicia(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @RequestBody NameUpdateRequest request) {
        log.info("PUT /api/franquicias/{}/nombre - Nuevo nombre: {}", franquiciaId, request.getNuevoNombre());
        return franquiciaService.actualizarNombreFranquicia(franquiciaId, request.getNuevoNombre())
                .doOnSuccess(f -> log.info("Nombre de franquicia actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar nombre de franquicia: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/nombre")
    @Operation(summary = "Actualizar nombre de sucursal", description = "Actualiza el nombre de una sucursal específica")
    public Mono<Franquicia> actualizarNombreSucursal(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @Parameter(description = "Nombre actual de la sucursal") @PathVariable String sucursalNombre, 
            @RequestBody NameUpdateRequest request) {
        log.info("PUT /api/franquicias/{}/sucursales/{}/nombre - Nuevo nombre: {}", 
                franquiciaId, sucursalNombre, request.getNuevoNombre());
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalNombre, request.getNuevoNombre())
                .doOnSuccess(f -> log.info("Nombre de sucursal actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar nombre de sucursal: {}", error.getMessage()));
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombre}/nombre")
    @Operation(summary = "Actualizar nombre de producto", description = "Actualiza el nombre de un producto específico")
    public Mono<Franquicia> actualizarNombreProducto(
            @Parameter(description = "ID de la franquicia") @PathVariable Integer franquiciaId, 
            @Parameter(description = "Nombre de la sucursal") @PathVariable String sucursalNombre, 
            @Parameter(description = "Nombre actual del producto") @PathVariable String productoNombre, 
            @RequestBody NameUpdateRequest request) {
        log.info("PUT /api/franquicias/{}/sucursales/{}/productos/{}/nombre - Nuevo nombre: {}", 
                franquiciaId, sucursalNombre, productoNombre, request.getNuevoNombre());
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalNombre, productoNombre, request.getNuevoNombre())
                .doOnSuccess(f -> log.info("Nombre de producto actualizado exitosamente"))
                .doOnError(error -> log.error("Error al actualizar nombre de producto: {}", error.getMessage()));
    }
}