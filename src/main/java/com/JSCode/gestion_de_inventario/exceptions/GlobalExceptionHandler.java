package com.JSCode.gestion_de_inventario.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.JSCode.gestion_de_inventario.dto.Response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ApiResponse<>(ex.getMessage(), true, ex.getStatus().value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>("Error interno del servidor", true, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
