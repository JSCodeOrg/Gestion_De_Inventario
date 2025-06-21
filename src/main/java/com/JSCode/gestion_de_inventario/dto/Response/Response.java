package com.JSCode.gestion_de_inventario.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta genérica del servidor")
public class Response<T> {
    @Schema(description = "Mensaje descriptivo del resultado de la operación", example = "Operación realizada con éxito")
    private String message;

    @Schema(description = "Datos específicos retornados en la respuesta (pueden variar según el endpoint)")
    private T data;

    @Schema(description = "Indica si hubo un error en la operación", example = "false")
    private boolean error;

    @Schema(description = "Código de estado HTTP personalizado", example = "200")
    private int status;
 
    public Response(String message, T data, boolean error, int status){
        this.message = message;
        this.data = data;
        this.error = error;
        this.status = status;
    }

    public Response(String message, boolean error, int status) {
        this(message, null, error, status);
    }

}
