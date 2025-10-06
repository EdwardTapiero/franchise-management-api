package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.BranchUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateBranchRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateBranchNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ErrorResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.BranchRestMapper;
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
@Tag(name = "Branches", description = "API para gestión de sucursales")
public class BranchController {

    private final BranchUseCase branchUseCase;
    private final BranchRestMapper mapper;

    @Operation(
            summary = "Agregar sucursal a franquicia",
            description = "Crea una nueva sucursal y la asocia a una franquicia existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sucursal creada exitosamente",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(
            value = "/franchises/{franchiseId}/branches",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> addBranchToFranchise(
            @Parameter(description = "ID de la franquicia", required = true)
            @PathVariable String franchiseId,
            @Parameter(description = "Datos de la sucursal a crear", required = true)
            @Valid @RequestBody CreateBranchRequest request) {

        log.info("REST: Adding branch '{}' to franchise id: {}", request.getName(), franchiseId);

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(branch -> branchUseCase.addBranchToFranchise(franchiseId, branch))
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch created with id: {}", response.getId()));
    }

    @Operation(
            summary = "Obtener sucursal por ID",
            description = "Obtiene los detalles de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursal encontrada",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/branches/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponse> getBranchById(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        log.info("REST: Getting branch by id: {}", id);

        return branchUseCase.getBranchById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch retrieved: {}", response.getName()));
    }

    @Operation(
            summary = "Listar sucursales de una franquicia",
            description = "Obtiene todas las sucursales asociadas a una franquicia"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de sucursales obtenido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/franchises/{franchiseId}/branches",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Flux<BranchResponse> getBranchesByFranchiseId(
            @Parameter(description = "ID de la franquicia", required = true)
            @PathVariable String franchiseId) {
        log.info("REST: Getting branches for franchise id: {}", franchiseId);

        return branchUseCase.getBranchesByFranchiseId(franchiseId)
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: Branches retrieved for franchise: {}", franchiseId));
    }

    @Operation(
            summary = "Actualizar nombre de sucursal",
            description = "Actualiza el nombre de una sucursal existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nombre inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping(
            value = "/branches/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<BranchResponse> updateBranchName(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id,
            @Parameter(description = "Nuevo nombre de la sucursal", required = true)
            @Valid @RequestBody UpdateBranchNameRequest request) {

        log.info("REST: Updating branch name. Id: {}, New name: {}", id, request.getName());

        return branchUseCase.updateBranchName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Branch name updated: {}", response.getName()));
    }

    @Operation(
            summary = "Eliminar sucursal",
            description = "Elimina una sucursal del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Sucursal eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/branches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBranch(
            @Parameter(description = "ID de la sucursal a eliminar", required = true)
            @PathVariable String id) {
        log.info("REST: Deleting branch with id: {}", id);

        return branchUseCase.deleteBranch(id)
                .doOnSuccess(v -> log.info("REST: Branch deleted: {}", id));
    }
}
