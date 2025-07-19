package com.linktic.productos.infraestructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.linktic.productos.domain.model.ErrorResponse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument_deberiaRetornar400() {
        String mensaje = "Error de argumento";
        IllegalArgumentException ex = new IllegalArgumentException(mensaje);

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensaje, response.getBody().getError());
    }

    @Test
    void handleInvalidJson_deberiaRetornar400() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("json mal");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidJson(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("JSON mal formado o incompleto", response.getBody().getError());
    }

    @Test
    void handleValidationErrors_deberiaRetornar400ConMensajeDeCampo() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("obj", "campo", "Campo es obligatorio");
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campo es obligatorio", response.getBody().getError());
    }

    @Test
    void handleValidationErrors_sinErroresDebeRetornarGenerico() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(null);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de validación", response.getBody().getError());
    }

    @Test
    void handleGeneric_deberiaRetornar500() {
        Exception ex = new RuntimeException("Fallo interno");

        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ocurrió un error inesperado", response.getBody().getError());
    }
}
