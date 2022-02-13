package com.regionalTourism.common;

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

    public void setStatus(String status) {
        switch (status){
            case Status.ERROR:
                this.message = Messages.GENERAL_ERROR;
                break;
            case Status.SUCCESS:
                this.message = Messages.GENERAL_SUCCESS;
                break;
            case Status.WARNING:
                this.message = Messages.GENERAL_WARNING;
                break;
        }
        this.status = status;
    }

    public String toJson(){
        return Constants.GSON.toJson(this);
    }
}
