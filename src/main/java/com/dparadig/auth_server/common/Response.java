package com.dparadig.auth_server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JLabarca
 * Estandar de respuesta para cada Request de la API
 * Debe ser siempre enviado como JSON con .toJson()
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String status;
    private String message;
    private Object data;

    public String toJson(){
        return Constants.GSON.toJson(this);
    }
}
