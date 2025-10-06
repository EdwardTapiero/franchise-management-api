package co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.handler;

import co.com.nequi.api.franchise_management_api.domain.exception.FranchiseNotFoundException;
import co.com.nequi.api.franchise_management_api.infrastructure.adapter.in.web.dto.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleFranchiseNotFoundException() {
        FranchiseNotFoundException exception = new FranchiseNotFoundException("franchise-1");
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        org.springframework.http.server.RequestPath path = mock(org.springframework.http.server.RequestPath.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/v1/franchises/franchise-1");

        ResponseEntity<ErrorResponse> response = handler.handleFranchiseNotFoundException(exception, exchange);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Franchise Not Found", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("franchise-1"));
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        org.springframework.http.server.RequestPath path = mock(org.springframework.http.server.RequestPath.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/v1/franchises");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(exception, exchange);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        org.springframework.http.server.reactive.ServerHttpRequest request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        org.springframework.http.server.RequestPath path = mock(org.springframework.http.server.RequestPath.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/v1/franchises");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception, exchange);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

}
