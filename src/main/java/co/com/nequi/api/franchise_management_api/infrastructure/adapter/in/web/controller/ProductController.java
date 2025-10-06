package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.domain.port.in.ProductUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateProductRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateProductNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ErrorResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.MaxStockProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.ProductRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API para gestión de productos")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final BranchUseCase branchUseCase;
    private final ProductRestMapper mapper;

    @Operation(
            summary = "Agregar producto a sucursal",
            description = "Crea un nuevo producto y lo asocia a una sucursal existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(
            value = "/branches/{branchId}/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProductToBranch(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String branchId,
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody CreateProductRequest request) {

        log.info("REST: Adding product '{}' to branch id: {}", request.getName(), branchId);

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(product -> productUseCase.addProductToBranch(branchId, product))
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product created with id: {}", response.getId()));
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Obtiene los detalles de un producto específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> getProductById(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id) {
        log.info("REST: Getting product by id: {}", id);

        return productUseCase.getProductById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product retrieved: {}", response.getName()));
    }

    @Operation(
            summary = "Listar productos de una sucursal",
            description = "Obtiene todos los productos de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de productos obtenido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/branches/{branchId}/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductResponse> getProductsByBranchId(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String branchId) {
        log.info("REST: Getting products for branch id: {}", branchId);

        return productUseCase.getProductsByBranchId(branchId)
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: Products retrieved for branch: {}", branchId));
    }

    @Operation(
            summary = "Modificar stock de producto",
            description = "Actualiza la cantidad de stock de un producto existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Stock inválido (no puede ser negativo)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping(
            value = "/products/{id}/stock",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> updateProductStock(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id,
            @Parameter(description = "Nueva cantidad de stock", required = true)
            @Valid @RequestBody UpdateProductStockRequest request) {

        log.info("REST: Updating product stock. Id: {}, New stock: {}", id, request.getStock());

        return productUseCase.updateProductStock(id, request.getStock())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product stock updated: {}", response.getStock()));
    }

    @Operation(
            summary = "Actualizar nombre de producto",
            description = "Actualiza el nombre de un producto existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nombre inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping(
            value = "/products/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> updateProductName(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id,
            @Parameter(description = "Nuevo nombre del producto", required = true)
            @Valid @RequestBody UpdateProductNameRequest request) {

        log.info("REST: Updating product name. Id: {}, New name: {}", id, request.getName());

        return productUseCase.updateProductName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Product name updated: {}", response.getName()));
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable String id) {
        log.info("REST: Deleting product with id: {}", id);

        return productUseCase.deleteProduct(id)
                .doOnSuccess(v -> log.info("REST: Product deleted: {}", id));
    }

    @Operation(
            summary = "Obtener productos con mayor stock por franquicia",
            description = "Retorna el producto con mayor stock de cada sucursal para una franquicia específica. " +
                    "Este endpoint demuestra el uso de operadores reactivos avanzados (flatMap, zip, map)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de productos con mayor stock por sucursal"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/franchises/{franchiseId}/max-stock-products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<MaxStockProductResponse> getMaxStockProductsByFranchise(
            @Parameter(description = "ID de la franquicia", required = true)
            @PathVariable String franchiseId) {

        log.info("REST: Getting max stock products for franchise id: {}", franchiseId);

        return productUseCase.getProductsWithMaxStockByFranchise(franchiseId)
                .flatMap(product -> branchUseCase.getBranchById(product.getBranchId())
                        .flatMap(branch -> mapper.toMaxStockResponse(product, Mono.just(branch))))
                .doOnComplete(() -> log.info("REST: Max stock products retrieved for franchise: {}", franchiseId));
    }
}