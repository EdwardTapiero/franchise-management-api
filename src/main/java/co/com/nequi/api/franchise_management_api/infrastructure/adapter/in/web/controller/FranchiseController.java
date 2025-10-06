package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.controller;

import co.com.nequi.api.franchise_management_api.domain.port.in.FranchiseUseCase;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.request.UpdateFranchiseNameRequest;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ErrorResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.mapper.FranchiseRestMapper;
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
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "API para gestión de franquicias")
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseRestMapper mapper;

    @Operation(
            summary = "Crear nueva franquicia",
            description = "Crea una nueva franquicia en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Franquicia creada exitosamente",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o franquicia ya existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(
            @Parameter(description = "Datos de la franquicia a crear", required = true)
            @Valid @RequestBody CreateFranchiseRequest request) {

        log.info("REST: Creating franchise with name: {}", request.getName());

        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(franchiseUseCase::createFranchise)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise created with id: {}", response.getId()));
    }

    @Operation(
            summary = "Obtener franquicia por ID",
            description = "Obtiene los detalles de una franquicia específica mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Franquicia encontrada",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponse> getFranchiseById(
            @Parameter(description = "ID de la franquicia", required = true)
            @PathVariable String id) {
        log.info("REST: Getting franchise by id: {}", id);

        return franchiseUseCase.getFranchiseById(id)
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise retrieved: {}", response.getName()));
    }

    @Operation(
            summary = "Listar todas las franquicias",
            description = "Obtiene un listado de todas las franquicias registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de franquicias obtenido exitosamente"
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<FranchiseResponse> getAllFranchises() {
        log.info("REST: Getting all franchises");

        return franchiseUseCase.getAllFranchises()
                .map(mapper::toResponse)
                .doOnComplete(() -> log.info("REST: All franchises retrieved"));
    }

    @Operation(
            summary = "Actualizar nombre de franquicia",
            description = "Actualiza el nombre de una franquicia existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nombre inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping(
            value = "/{id}/name",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<FranchiseResponse> updateFranchiseName(
            @Parameter(description = "ID de la franquicia", required = true)
            @PathVariable String id,
            @Parameter(description = "Nuevo nombre de la franquicia", required = true)
            @Valid @RequestBody UpdateFranchiseNameRequest request) {

        log.info("REST: Updating franchise name. Id: {}, New name: {}", id, request.getName());

        return franchiseUseCase.updateFranchiseName(id, request.getName())
                .map(mapper::toResponse)
                .doOnSuccess(response -> log.info("REST: Franchise name updated: {}", response.getName()));
    }

    @Operation(
            summary = "Eliminar franquicia",
            description = "Elimina una franquicia del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Franquicia eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Franquicia no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteFranchise(
            @Parameter(description = "ID de la franquicia a eliminar", required = true)
            @PathVariable String id) {
        log.info("REST: Deleting franchise with id: {}", id);

        return franchiseUseCase.deleteFranchise(id)
                .doOnSuccess(v -> log.info("REST: Franchise deleted: {}", id));
    }

}
